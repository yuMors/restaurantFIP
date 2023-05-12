<template>
  <el-card class="main-card">
    <div class="title">

      {{ this.$route.name }}
<!--      <a style="background: #6bcafb">aaaa</a>-->
    </div>
<!--    <div class="article-status-menu">-->
<!--      <span>状态：</span>-->
<!--      <span @click="changeStatus('all')" :class="isActive('all')">全部</span>-->
<!--      <span @click="changeStatus('public')" :class="isActive('public')"> 公开 </span>-->
<!--      <span @click="changeStatus('private')" :class="isActive('private')"> 私密 </span>-->

<!--    </div>-->
    <div class="operation-container">
<!--      <el-button-->
<!--          type="success"-->
<!--          size="small"-->
<!--          icon="el-icon-plus"-->
<!--          style="margin-right: 1rem"-->
<!--          @click="isAdd = true">-->
<!--        新增菜谱-->
<!--      </el-button>-->
      <el-button
          type="danger"
          size="small"
          icon="el-icon-delete"
          :disabled="articleIds.length == 0"
          @click="updateIsDelete = true">
        批量删除
      </el-button>
<!--      <el-button-->
<!--          type="success"-->
<!--          size="small"-->
<!--          icon="el-icon-download"-->
<!--          :disabled="articleIds.length == 0"-->
<!--          style="margin-right: 1rem"-->
<!--          @click="isExport = true">-->
<!--        批量导出-->
<!--      </el-button>-->

      <div style="margin-left: auto">

        <el-select
            clearable
            size="small"
            v-model="categoryId"
            filterable
            placeholder="请选择分类"
            style="margin-right: 1rem; width: 180px">
          <el-option label="全部" value=""/>
          <el-option v-for="item in categories" :key="item.id" :label="item.categoryName" :value="item.id"/>
        </el-select>

        <el-input
            clearable
            v-model="keywords"
            prefix-icon="el-icon-search"
            size="small"
            placeholder="请输入名称"
            style="width: 200px"
            @keyup.enter.native="searchArticles"/>
        <el-button type="primary" size="small" icon="el-icon-search" style="margin-left: 1rem" @click="searchArticles">
          搜索
        </el-button>
      </div>
    </div>
    <el-table border :data="articles" @selection-change="selectionChange" v-loading="loading">
      <el-table-column type="selection" width="55"/>

      <el-table-column prop="articleCover" label="封面" width="180" align="center">
        <template slot-scope="scope">
          <!--          t<el-image-->
          <!--              class="article-cover"-->
          <!--              v-if=""-->
          <!--              :src="-->
          <!--              scope.row.articleCover-->
          <!--                ? scope.row.articleCover-->
          <!--                // : 'http://static.talkxj.com/articles/c5cc2b2561bd0e3060a500198a4ad37d.png'-->
          <!--                : 'http://rs81ljuv5.hb-bkt.clouddn.com/2023/03/28/20230328-170921YH-d73d57c2a22ef2.png'-->
          <!--          http://rs81ljuv5.hb-bkt.clouddn.com/2023/03/28/20230328-170921YH-d73d57c2a22ef2.png-->
          <!--            "/>-->
          <el-image
              :src="scope.row.articleCover">
          </el-image>
          <!--<i v-if="scope.row.status == 1" class="iconfont el-icon-mygongkai article-status-icon"/>-->
          <!--<i v-if="scope.row.status == 2" class="iconfont el-icon-mymima article-status-icon"/>-->
          <!--<i v-if="scope.row.status == 3" class="iconfont el-icon-mycaogaoxiang article-status-icon"/>-->
        </template>
      </el-table-column>
      <el-table-column prop="articleTitle" label="标题" align="center"/>
      <el-table-column prop="categoryName" label="分类" width="160" align="center"/>
      <el-table-column prop="tagDTOs" label="标签" width="170" align="center">
        <template slot-scope="scope">
          <el-tag v-for="item of scope.row.tagDTOs" :key="item.tagId" style="margin-right: 0.2rem; margin-top: 0.2rem">
            {{ item.tagName }}
          </el-tag>
        </template>
      </el-table-column>
      <el-table-column prop="viewsCount" label="浏览量" width="70" align="center">
        <template slot-scope="scope">
          <span v-if="scope.row.viewsCount">
            {{ scope.row.viewsCount }}
          </span>
          <span v-else>0</span>
        </template>
      </el-table-column>
      <el-table-column prop="type" label="类型" width="80" align="center">
        <template slot-scope="scope">
          <el-tag :type="articleType(scope.row.type).tagType">
            {{ articleType(scope.row.type).name }}
          </el-tag>
        </template>
      </el-table-column>
      <el-table-column prop="createTime" label="发表时间" width="130" align="center">
        <template slot-scope="scope">
          <i class="el-icon-time" style="margin-right: 5px"/>
          {{ scope.row.createTime | date }}
        </template>
      </el-table-column>
      <el-table-column prop="isTop" label="置顶" width="80" align="center">
        <template slot-scope="scope">
          <el-switch
              v-model="scope.row.isTop"
              active-color="#13ce66"
              inactive-color="#F4F4F5"
              :disabled="scope.row.isDelete == 1"
              :active-value="1"
              :inactive-value="0"
              @change="changeTopAndFeatured(scope.row)"/>
        </template>
      </el-table-column>
      <el-table-column prop="isFeatured" label="推荐" width="80" align="center">
        <template slot-scope="scope">
          <el-switch
              v-model="scope.row.isFeatured"
              active-color="#13ce66"
              inactive-color="#F4F4F5"
              :disabled="scope.row.isDelete == 1"
              :active-value="1"
              :inactive-value="0"
              @change="changeTopAndFeatured(scope.row)"/>
        </template>
      </el-table-column>
      <el-table-column label="操作" align="center" width="150">
        <template slot-scope="scope">
          <el-button type="primary" size="mini" @click="editArticle(scope.row.id)" v-if="scope.row.isDelete == 0">
            编辑
          </el-button>
          <el-popconfirm
              title="确定删除吗？"
              cancel-button-type=""
              style="margin-left: 10px"
              @confirm="updateArticleDelete(scope.row.id)"
              v-if="scope.row.isDelete == 0">
            <el-button size="mini" type="danger" slot="reference"> 删除</el-button>
          </el-popconfirm>
          <el-popconfirm
              title="确定恢复吗？"
              v-if="scope.row.isDelete == 1"
              @confirm="updateArticleDelete(scope.row.id)">
            <el-button size="mini" type="success" slot="reference"> 恢复</el-button>
          </el-popconfirm>
          <el-popconfirm
              style="margin-left: 10px"
              v-if="scope.row.isDelete == 1"
              title="确定彻底删除吗？"
              @confirm="deleteArticles(scope.row.id)">
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

    <el-dialog :visible.sync="updateIsDelete" width="30%">
      <div class="dialog-title-container" slot="title"><i class="el-icon-warning" style="color: #ff9900"/>提示</div>
      <div style="font-size: 1rem">是否删除选中项？</div>
      <div slot="footer">
        <el-button @click="updateIsDelete = false">取 消</el-button>
        <el-button type="primary" @click="updateArticleDelete(null)"> 确 定</el-button>
      </div>
    </el-dialog>

    <el-dialog :visible.sync="remove" width="30%">
      <div class="dialog-title-container" slot="title"><i class="el-icon-warning" style="color: #ff9900"/>提示</div>
      <div style="font-size: 1rem">是否彻底删除选中项？</div>
      <div slot="footer">
        <el-button @click="remove = false">取 消</el-button>
        <el-button type="primary" @click="deleteArticles(null)"> 确 定</el-button>
      </div>
    </el-dialog>

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
    editArticle(id) {
      this.$router.push({path: '/articles/' + id})
    },
    updateArticleDelete(id) {
      let param = {}
      if (id != null) {
        param.ids = [id]
      } else {
        param.ids = this.articleIds
      }
      param.isDelete = this.isDelete == 0 ? 1 : 0
      this.axios.put('/api/admin/articles', param).then(({data}) => {
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
          .get('/api/admin/articles', {
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
            this.articles = data.data.records
            this.count = data.data.count
            this.loading = false
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
