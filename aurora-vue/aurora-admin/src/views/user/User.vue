<template>

  <el-card class="main-card">
    <div class="title">{{ this.$route.name }}</div>
    <div class="operation-container">
      <div style="margin-left: auto">

        <el-input v-model="keywords" prefix-icon="el-icon-search" size="small" placeholder="请输入昵称" clearable style="width: 200px"
          @keyup.enter.native="searchUsers" />
        <el-button type="primary" size="small" icon="el-icon-search" style="margin-left: 1rem" @click="searchUsers">
          搜索
        </el-button>
      </div>
    </div>

    <!-- 列表 -->
    <el-table border :data="userList" v-loading="loading">
      <el-table-column
          align="center"
          label="序号"
          width="70px">
        <template slot-scope="scopes">
          {{ scopes.$index+1 }}
        </template>
      </el-table-column>


      <el-table-column prop="linkAvatar" label="头像" align="center" width="150">
        <template slot-scope="scope">
          <img :src="scope.row.avatar" width="70" height="50" />
        </template>
      </el-table-column>

      <el-table-column prop="nickname" label="昵称" align="center" width="140" />

      <el-table-column prop="username" label="账号" align="center" width="140" />

      <el-table-column prop="roles" label="用户角色" align="center" width="">
        <template slot-scope="scope">
          <el-tag v-for="(item, index) of scope.row.roles" :key="index" style="margin-right: 4px; margin-top: 4px">
            {{ item.roleName }}
          </el-tag>
        </template>
      </el-table-column>
<!--      <el-table-column prop="isDisable" label="禁用" align="center" width="100">-->
<!--        <template slot-scope="scope">-->
<!--          <el-switch v-model="scope.row.isDisable" active-color="#13ce66" inactive-color="#F4F4F5" :active-value="1"-->
<!--            :inactive-value="0" @change="changeDisable(scope.row)" />-->
<!--        </template>-->
<!--      </el-table-column>-->
      <el-table-column show-tooltip-when-overflow
          label="描述" prop="intro" align="center" width="260"/>

<!--      <el-table-column prop="ipAddress" label="登录ip" align="center" width="140" />-->
<!--      <el-table-column prop="ipSource" label="登录地址" align="center" width="140" />-->
      <el-table-column prop="createTime" label="创建时间" width="190" align="center">
        <template slot-scope="scope">
          <i class="el-icon-time" style="margin-right: 3px" />
          {{ scope.row.createTime | dateTime }}
        </template>
      </el-table-column>
      <el-table-column prop="lastLoginTime" label="上次登录时间" width="130" align="center">
        <template slot-scope="scope">
          <i class="el-icon-time" style="margin-right: 5px" />
          {{ scope.row.lastLoginTime | date }}
        </template>
      </el-table-column>
      <el-table-column label="操作" align="center" width="180">
        <template slot-scope="scope">
          <el-button type="primary" size="mini" @click="openEditModel(scope.row)"> 编辑 </el-button>

          <el-popconfirm
              title="确定删除吗？"
              cancel-button-type=""
              style="margin-left: 10px"
              @confirm="deleteUser(scope.row.userInfoId)">
            <el-button size="mini" type="danger" slot="reference"> 删除</el-button>
          </el-popconfirm>
        </template>

      </el-table-column>

    </el-table>
    <el-pagination class="pagination-container" background @size-change="sizeChange" @current-change="currentChange"
      :current-page="current" :page-size="size" :total="count" :page-sizes="[10, 20]"
      layout="total, sizes, prev, pager, next, jumper" />

    <el-dialog :visible.sync="isEdit" width="30%">
      <div class="dialog-title-container" slot="title">修改用户</div>
      <el-form label-width="60px" size="medium" :model="userForm">
        <el-form-item label="昵称">
          <el-input v-model="userForm.nickname" style="width: 220px" />
        </el-form-item>

        <el-form-item label="角色">
          <el-radio-group v-model="roleIds">
            <el-radio v-for="item of userRoles" :key="item.id" :label="item.id">
              {{ item.roleName }}
            </el-radio>
          </el-radio-group>
        </el-form-item>
      </el-form>
      <div slot="footer">
        <el-button @click="isEdit = false">取 消</el-button>
        <el-button type="primary" @click="editUserRole"> 确 定 </el-button>
      </div>
    </el-dialog>

  </el-card>
</template>

<script>
export default {
  created() {
    this.current = this.$store.state.pageState.user
    this.listUsers()
    this.listRoles()
  },
  data: function () {
    return {
      loading: true,
      isEdit: false,
      userForm: {
        userInfoId: null,
        nickname: '',
        roleIds: '',
      },
      modelForm: {
        userInfoId: null,
        nickname: '',
        roleIds: '',
      },
      loginType: null,
      userRoles: [],
      roleIds: '',
      userList: [],
      typeList: [
        {
          type: 1,
          desc: '邮箱'
        },
        {
          type: 2,
          desc: 'QQ'
        }
      ],
      keywords: null,
      current: 1,
      size: 10,
      count: 0
    }
  },
  methods: {
    deleteUser(id){
      console.log('id ',id)
      this.axios.get('/api/admin/userDelete?id='+id).then(({data}) => {
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
      })
    },
    searchUsers() {
      this.current = 1
      this.listUsers()
    },
    sizeChange(size) {
      this.size = size
      this.listUsers()
    },
    currentChange(current) {
      this.current = current
      this.$store.commit('updateUserPageState', current)
      this.listUsers()
    },
    changeDisable(user) {
      this.axios.put('/api/admin/users/disable', {
        id: user.userInfoId,
        isDisable: user.isDisable
      })
    },
    openEditModel(user) {
      console.log('user',user)
      this.roleIds = user.roles[0].id;
      this.userForm = JSON.parse(JSON.stringify(user))
      console.log('打开编辑时',this.userForm.roles)
      // this.userForm.roleIds => this.roleIds
      this.userForm.roleIds = this.roleIds;
      console.log('打开编辑时',this.userForm.roles)
      // this.userForm.roles.forEach((item) => {
      //   this.roleIds.push(item.id)
      // })
      this.isEdit = true
    },
    editUserRole() {
      this.userForm.roleIds = this.roleIds
      this.modelForm.userInfoId = this.userForm.userInfoId;
      this.modelForm.nickname = this.userForm.nickname;
      this.modelForm.roleIds = this.userForm.roleIds;
      console.log('this.modelForm',this.modelForm)
      this.axios.put('/api/admin/users/role', this.modelForm).then(({ data }) => {
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
    listUsers() {
      this.axios.get('/api/admin/users', {
        params: {
          current: this.current,
          size: this.size,
          keywords: this.keywords,
          loginType: this.loginType
        }
      }).then(({ data }) => {
        this.userList = data.data.records
        this.count = data.data.count
        this.loading = false
      })
    },
    listRoles() {
      this.axios.get('/api/admin/users/role').then(({ data }) => {
        this.userRoles = data.data
      })
    }
  },
  watch: {
    loginType() {
      this.current = 1
      this.listUsers()
    }
  }
}
</script>
