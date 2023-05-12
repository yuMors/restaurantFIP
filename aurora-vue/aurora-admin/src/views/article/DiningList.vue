<template>
  <el-card class="main-card">
    <div class="title">
      {{ this.$route.name }}
      <a style="background: #6bcafb">aaaa</a>
    </div>
    <br/>
    <div class="operation-container">
<!--      <el-button-->
<!--          type="success"-->
<!--          size="small"-->
<!--          icon="el-icon-plus"-->
<!--          style="margin-right: 1rem"-->
<!--          @click="addCp">-->
<!--        新增菜谱-->
<!--      </el-button>-->

      <div style="margin-left: auto">

        <el-select
            clearable
            size="small"
            v-model="categoryId"
            filterable
            placeholder="请选择分类"
            style="margin-right: 1rem; width: 180px">
<!--          <el-option label="全部" value=""/>-->
          <el-option v-for="item in categories" :key="item.id" :label="item.categoryName" :value="item.id"/>
        </el-select>

        <el-input
            clearable
            v-model="keywords"
            prefix-icon="el-icon-search"
            size="small"
            placeholder="请输入菜谱名"
            style="width: 200px"
            @keyup.enter.native="searchArticles"/>
        <el-button type="primary" size="small" icon="el-icon-search" style="margin-left: 1rem" @click="searchArticles">
          搜索
        </el-button>
      </div>
    </div>
    <el-table border :data="articles" @selection-change="selectionChange" v-loading="loading">
      <el-table-column type="selection" width="55"/>

      <el-table-column prop="articleCover" label="文章封面" width="180" align="center">
        <template slot-scope="scope">
          <el-image
              :src="scope.row.articleCover">
          </el-image>
        </template>
      </el-table-column>
      <el-table-column prop="articleTitle" label="标题" align="center"/>
      <el-table-column prop="price" label="价格" align="center"/>

      <el-table-column prop="desc" label="描述" align="center" show-tooltip-when-overflow>
      </el-table-column>
      <el-table-column prop="categoryName" label="分类" width="110" align="center">
<!--        <template slot-scope="scope">-->
<!--          <el-tag v style="margin-right: 0.2rem; margin-top: 0.2rem">-->
<!--            {{ categoryName }}-->
<!--          </el-tag>-->
<!--        </template>-->
<!--        <template slot-scope="scope">-->
<!--          <el-tag :type="articleType(scope.row.type).tagType">-->
<!--            {{ articleType(scope.row.type).name }}-->
<!--          </el-tag>-->
<!--        </template>-->
      </el-table-column>


      <el-table-column prop="viewsCount" label="浏览量" width="70" align="center">
        <template slot-scope="scope">
          <span v-if="scope.row.viewsCount">
            {{ scope.row.viewsCount }}
          </span>
          <span v-else>0</span>
        </template>
      </el-table-column>

      <el-table-column prop="createTime" label="发表时间" width="130" align="center">
        <template slot-scope="scope">
          <i class="el-icon-time" style="margin-right: 5px"/>
          {{ scope.row.createTime | date }}
        </template>
      </el-table-column>

<!--      <el-table-column prop="isFeatured" label="推荐" width="80" align="center">-->
<!--      </el-table-column>-->
      <el-table-column label="操作" align="center" width="150">
        <template slot-scope="scope">
          <el-button type="primary" size="mini" @click="editArticle(scope.row.id)">
            编辑
          </el-button>
          <el-popconfirm
              title="确定删除吗？"
              cancel-button-type=""
              style="margin-left: 10px"
              @confirm="updateArticleDelete(scope.row.id)">
            <el-button size="mini" type="danger" slot="reference"> 删除</el-button>
          </el-popconfirm>
        </template>
      </el-table-column>
    </el-table>
    <el-pagination
        class="pagination-container"
        background
        @size-change="sizeChange"
        @current-change="currentChange"
        :current-page="current"
        :page-size="size"
        :total="count"
        :page-sizes="[10, 20]"
        layout="total, sizes, prev, pager, next, jumper"/>




    <el-dialog :visible.sync="isAdd" width="30%">
      <div class="dialog-title-container" slot="title">修改用户</div>
      <el-form label-width="60px" size="medium" :model="restaurantForm">
        <el-form-item label="">
          <el-input v-model="restaurantForm.nickname" style="width: 220px" />
        </el-form-item>

        <!--        <el-form-item label="角色">-->
        <!--          <el-radio-group v-model="roleIds">-->
        <!--            <el-radio v-for="item of userRoles" :key="item.id" :label="item.id">-->
        <!--              {{ item.roleName }}-->
        <!--            </el-radio>-->
        <!--          </el-radio-group>-->
        <!--        </el-form-item>-->
      </el-form>
      <div slot="footer">
        <el-button @click="isAdd = false">取 消</el-button>
        <el-button type="primary" @click="editUserRole"> 确 定 </el-button>
      </div>
    </el-dialog>

  </el-card>
</template>

<script>
export default {
  name: "DiningList",//这里放的是菜谱列表

  data: function () {
    return {
      uploadHeaders: {Authorization: 'Bearer ' + sessionStorage.getItem('token')},
      loading: true,
      updateIsDelete: false,
      remove: false,
      isAdd: false,
      //新增弹窗
      restaurantForm: {
        userInfoId: null,
        nickname: '',
        roleIds: '',
      },
      types: [
        {
          value: 1,
          label: '原创'
        },
        {
          value: 2,
          label: '转载'
        },
        {
          value: 3,
          label: '翻译'
        }
      ],
      activeStatus: 'all',
      articles: [],
      articleIds: [],
      categories: [],
      tags: [],
      keywords: null,
      type: null,
      categoryId: null,
      tagId: null,
      isDelete: 0,
      // isExport: false,
      status: null,
      current: 1,
      size: 10,
      count: 0
    }
  },
  created() {
    this.current = this.$store.state.pageState.articleList
    //查询所有文章
    this.listArticles()
    //查询所有分类
    this.listCategories()
    //查询所有标签
    this.listTags()
  },
  methods: {
    addCp(){
      this.$router.push('/article/RestaurantList');
    },
    //打开编辑弹窗即新增弹窗
    openEditModel(user) {
      console.log('user',user)
      // this.roleIds = user.roles[0].id;
      // this.userForm = JSON.parse(JSON.stringify(user))
      // console.log('打开编辑时',this.userForm.roles)
      // this.userForm.roleIds => this.roleIds
      // this.userForm.roleIds = this.roleIds;
      // console.log('打开编辑时',this.userForm.roles)
      // this.userForm.roles.forEach((item) => {
      //   this.roleIds.push(item.id)
      // })
      this.isEdit = true
    },
    //新增保存按钮
    editUserRole() {

      this.axios.put('/api/admin/users/addaaaaaa').then(({ data }) => {
        if (data.flag) {
          this.$notify.success({
            title: '成功',
            message: data.message
          })
          this.listUsers()
        } else {
          this.$notify.error({
            title: '失败',
            message: data.message
          })
        }
        this.isEdit = false
      })
    },
    selectionChange(articles) {
      this.articleIds = []
      articles.forEach((item) => {
        this.articleIds.push(item.id)
      })
    },
    searchArticles() {
      this.current = 1
      this.listArticles()
    },
    //编辑
    editArticle(id) {
      this.$router.push(
          {path: '/article/RestaurantList',query:{id: id}})
    },
    updateArticleDelete(id) {
      let param = {}
      if (id != null) {
        param.ids = [id]
      } else {
        param.ids = this.articleIds
      }
      this.axios.get('/api/restaurant/deleteRes', {
        params:{
          id: id,
        }
      }).then(({data}) => {
        if (data.flag) {
          this.$notify.success({
            title: '删除成功',
            message: data.message
          })
          this.listArticles()
        } else {
          this.$notify.error({
            title: '删除失败',
            message: data.message
          })
        }
        this.updateIsDelete = false
      })
    },
    deleteArticles(id) {
      let param = {}
      if (id == null) {
        param = {data: this.articleIds}
      } else {
        param = {data: [id]}
      }
      this.axios.delete('/api/admin/articles/delete', param).then(({data}) => {
        if (data.flag) {
          this.$notify.success({
            title: '成功',
            message: data.message
          })
          this.listArticles()
        } else {
          this.$notify.error({
            title: '失败',
            message: data.message
          })
        }
        this.remove = false
      })
    },

    sizeChange(size) {
      this.size = size
      this.listArticles()
    },
    currentChange(current) {
      this.current = current
      //这句话中 $store 是 Vuex 中的 store 实例，commit 方法用于触发状态的更改，
      //通常传入两个参数：第一个是 mutation 的名称，第二个是传给 mutation 的参数（可选）。
      //该方法用于在组件中执行状态更改
      this.$store.commit('updateArticleListPageState', current)
      this.listArticles()
    },
    changeStatus(status) {
      switch (status) {
        case 'all':
          this.isDelete = 0
          this.status = null
          break
        case 'public':
          this.isDelete = 0
          this.status = 1
          break
        case 'private':
          this.isDelete = 0
          this.status = 2
          break
        case 'draft':
          this.isDelete = 0
          this.status = 3
          break
        case 'delete':
          this.isDelete = 1
          this.status = null
          break
      }
      this.current = 1
      this.activeStatus = status
    },
    //
    changeTopAndFeatured(article) {
      this.axios.put('/api/admin/articles/topAndFeatured', {
        id: article.id,
        isTop: article.isTop,
        isFeatured: article.isFeatured
      }).then(({data}) => {
        if (data.flag) {
          this.$notify.success({
            title: '成功',
            message: '修改成功'
          })
        } else {
          this.$notify.error({
            title: '失败',
            message: data.message
          })
        }
        this.remove = false
      })
    },
    //查询所有文章
    listArticles() {
      this.axios
          .get('/api/admin/articles/dinglist', {
            params: {
              current: this.current,
              size: this.size,
              keywords: this.keywords,
              categoryId: this.categoryId,
              status: this.status,
              tagId: this.tagId,
              type: this.type,
              isDelete: this.isDelete,
              userAuthId: this.$store.state.userInfo.id,
            }
          })
          .then(({data}) => {
            if (data.flag){
              this.articles = data.data.records
              this.count = data.data.count
              this.loading = false
            } else {
              this.$notify.error({
                title: '失败',
                message: data.message
              })
              this.loading = false
            }
          })
    },
    //查询所有分类
    listCategories() {
      this.axios.get('/api/admin/categories/search').then(({data}) => {
        this.categories = data.data
      })
    },
    //查询所有标签
    listTags() {
      this.axios.get('/api/admin/tags/search').then(({data}) => {
        this.tags = data.data
      })
    }
  },
  watch: {
    type() {
      this.current = 1
      this.listArticles()
    },
    categoryId() {
      this.current = 1
      this.listArticles()
    },
    tagId() {
      this.current = 1
      this.listArticles()
    },
    status() {
      this.current = 1
      this.listArticles()
    },
    isDelete() {
      this.current = 1
      this.listArticles()
    }
  },
  computed: {
    articleType() {
      return function (type) {
        var tagType = ''
        var name = ''
        switch (type) {
          case 1:
            tagType = 'danger'
            name = '原创'
            break
          case 2:
            tagType = 'success'
            name = '转载'
            break
          case 3:
            tagType = 'primary'
            name = '翻译'
            break
        }
        return {
          tagType: tagType,
          name: name
        }
      }
    },
    isActive() {
      return function (status) {
        return this.activeStatus == status ? 'active-status' : 'status'
      }
    }
  }
}
</script>

<style scoped>
.operation-container {
  margin-top: 1.5rem;
}

.article-status-menu {
  font-size: 14px;
  margin-top: 40px;
  color: #999;
}

.article-status-menu span {
  margin-right: 24px;
}

.status {
  cursor: pointer;
}

.active-status {
  cursor: pointer;
  color: #333;
  font-weight: bold;
}

.article-cover {
  position: relative;
  width: 100%;
  height: 90px;
  border-radius: 4px;
}

.article-cover::after {
  content: '';
  background: rgba(0, 0, 0, 0.3);
  position: absolute;
  top: 0;
  bottom: 0;
  left: 0;
  right: 0;
}

.article-status-icon {
  color: #fff;
  font-size: 1.5rem;
  position: absolute;
  right: 1rem;
  bottom: 1.4rem;
}
</style>
