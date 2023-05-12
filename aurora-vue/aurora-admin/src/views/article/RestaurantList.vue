<template>

  <el-card class="main-card">
    <div class="title">
     {{ this.$route.name }}<!-- 菜谱列表 -->
<!--      新增菜谱-->
      <a style="background: #6bcafb">aaaa</a>
    </div>
    <br/>
    <el-form :model="formData" ref="form" label-width="80px" :rules="rules">
      <el-form-item label="菜谱名称" prop="cpName" style="margin-top: 2%;">
        <el-input v-model="formData.cpName" style="width: 30%" />
      </el-form-item>
<!--      <el-form-item label="菜谱分类" prop="cpType">-->
<!--        <el-select v-model="formData.cpType" placeholder="请选择分类">-->
<!--          <el-option v-for="item in typeList" :key="item" :label="item" :value="item"/>-->
<!--        </el-select>-->
<!--      </el-form-item>-->
      <el-form-item label="价格：" prop="cpPrice">
        <el-input v-model="formData.cpPrice" placeholder="" style="width: 30%" />
      </el-form-item>
      <el-form-item label="菜谱描述" prop="cpDesc" style="margin-top: 2%;">
        <el-input type="textarea" :rows="2" v-model="formData.cpDesc" label="aa" placeholder="请输入内容" style="width: 30%" />
      </el-form-item>
<!--      <el-form-item label="用户密码" prop="password">-->
<!--        <el-input v-model="formData.password" placeholder="可以不填，默认:123456" style="width: 30%" />-->
<!--      </el-form-item>-->
      <el-form-item label="样例图片" prop="avatar">
        <el-upload class="avatar-uploader" action="/api/admin/articles/images" :show-file-list="false" :headers="headers"
                   :on-success="handleAvatarSuccess" :before-upload="beforeAvatarUpload">
          <img v-if="avatar" :src="avatar" class="avatar">
          <i v-else class="el-icon-plus avatar-uploader-icon"></i>
        </el-upload>
      </el-form-item>
      <el-form-item>
        <el-button type="primary" @click="addUser"> {{formData.id ? '修改' : '新增'}}</el-button>
      </el-form-item>
    </el-form>

  </el-card>
</template>

<script>
export default {
  name: "RestaurantList",//这个是新增菜谱的地方
  data: function () {
    return {
      typeList: [],
      uploadHeaders: {Authorization: 'Bearer ' + sessionStorage.getItem('token')},
      headers: { Authorization: 'Bearer ' + sessionStorage.getItem('token') },
      formData: {
        id: '',
        cpName: '',
        cpPrice: '',
        cpType: '',
        cpDesc: '',
        avatar: null,
        userAuthId: this.$store.state.userInfo.id,
      },
      articles: {
        id: '',
        cpName: '',
        cpPrice: '',
        cpDesc: '',
        avatar: '',
        userAuthId: '',
      },
      rules: {
        cpName: [
          //'trigger: "blur"' 是在 element-ui 的表单验证规则中用来指定验证触发方式的一种方式。
          // 在这种方式下，当表单控件失去焦点时就会触发该控件的验证。
          { required: true, message: '请输入用户名', trigger: 'blur' },
          { min: 1, max: 20, message: '长度在 1 到 20 个字符', trigger: 'blur' },
          { pattern: /^[^\s]*$/, message: '不能包含空格', trigger: 'blur'}
        ],
        cpDesc: [
          { required: true, message: '请输入描述', trigger: 'blur' },
          // { pattern: /^[^\s]*$/, message: '不能包含空格', trigger: 'blur'}
          // { pattern: /^[0-9]+$/, message: '请输入数字', trigger: 'blur' }
        ],
        cpPrice: [
          { required: true, message: '请输入价格', trigger: 'blur' },
          { pattern: /^[^\s]*$/, message: '不能包含空格', trigger: 'blur'},
          { pattern: /(^[1-9]\d*(\.\d{1,2})?$)|(^0(\.\d{1,2})?$)/, message: '请输入数字,最多两位小数', trigger: 'blur'}
          // { pattern: /^[0-9]+$/, message: '请输入数字', trigger: 'blur' },
        ],
        avatar: [
          { required: true, message: '请选择照片', trigger: 'blur' },
        ]
      },

    }
  },
  created() {

    // this.axios.get('/api/restaurant/typeList').then(response => {
    //   console.log(response);
    //   if(response.data.flag){
    //     //this.$message.success('用 // 跳转到用户列表页面
    //     this.typeList = response.data
    //   }else {
    //     this.$message.error(response.data.message)
    //   }
    // }).catch(error => {
    //   console.error(error);
    // });

    //this.current = this.$store.state.pageState.articleList
    //查询所有文章
    // var store = this.$store.state.userInfo;
    // console.log('store ',store)
    let cpid = this.$route.query.id;
    // var id = cpid.toString();
    console.log('id id ', cpid)
    // console.log(id)
    if (cpid) {
      //this.axios.get('/api/admin/userDelete?id='+id)
      // this.axios.get('/api/admin/articles/restaurantId?id='+cpid)
      //headers: { Authorization: 'Bearer ' + sessionStorage.getItem('token') }
      this.axios.get('/api/restaurant/admin/getResById?id='+cpid).then(({data}) => {
        console.log('get data',data)
        this.formData = data.data
      })
    }

    // } else {
      //const article = sessionStorage.getItem('article')
      // console.log('session ',sessionStorage)
      // if (article) {
      //   this.article = JSON.parse(article)
      // }
    // }
  },

  methods: {
    addUser() {
      if (this.formData.id){
        this.$refs.form.validate(valid => {
          if (valid) {
            // 发送 POST 请求将表单数据上传到服务器
            this.axios.post('/api/restaurant/updateRes', this.formData)
                .then(response => {
                  console.log(response);
                  if(response.data.flag){
                    this.$message.success('用户修改成功');
                    // 跳转到用户列表页面
                    this.$router.push('/article/DiningList');
                  }else {
                    this.$message.error(response.data.message)
                  }
                }).catch(error => {
              console.error(error);
            });
          } else {
            return false;
          }
        });
      } else {
        this.$refs.form.validate(valid => {
          if (valid) {
            // 发送 POST 请求将表单数据上传到服务器
            this.axios.post('/api/restaurant/addres', this.formData)
                .then(response => {
                  console.log(response);
                  if(response.data.flag){
                    this.$message.success('用户新增成功');
                    // 跳转到用户列表页面
                    this.$router.push('/article/DiningList');
                  }else {
                    this.$message.error(response.data.message)
                  }

                })
                .catch(error => {
                  console.error(error);
                });
          } else {
            return false;
          }
        });

      }

    },
    handleFileUpload(event) {
      // 从文件输入框中获取用户上传的头像文件
      const file = event.target.files[0];
      // 将头像文件保存到组件的 data 中
      this.avatar = file;
    },
    handleAvatarSuccess(response, file, fileList) {
      console.log(response, file, fileList, 88888);
      this.formData.avatar = response.data;
      this.$message.success('图片上传成功！');
    },
    handleUploadError(error, file, fileList) {
      console.error(error);
      this.$message.error('图片上传失败！');
    },
    beforeAvatarUpload(file) {
      console.log(file, 333);
      const isJPG = file.type === 'image/jpeg';
      const isPNG = file.type === 'image/png'
      console.log(isPNG, 333111);
      const isLt2M = file.size / 1024 / 1024 < 2;

      if (!isJPG && !isPNG) {
        this.$message.error('上传头像图片只能是 JPG 或 png格式!');
      }
      if (!isLt2M) {
        this.$message.error('上传头像图片大小不能超过 2MB!');
      }
      return (isJPG || isPNG) && isLt2M;
    },
  },
  computed: {
    avatar() {
      return this.formData.avatar
    }
  }
}
</script>

<style scoped>
.avatar-uploader .el-upload {
  border: 3px dashed #d9d9d9;
  border-radius: 6px;
  cursor: pointer;
  position: relative;
  overflow: hidden;
}

.avatar-uploader .el-upload:hover {
  border-color: #409EFF;
}

.avatar-uploader-icon {
  font-size: 28px;
  color: #8c939d;
  width: 178px;
  height: 178px;
  line-height: 178px;
  text-align: center;
  border: 1px solid rgba(0, 0, 0, 0.2);
}

.avatar {
  width: 178px;
  height: 178px;
  display: block;
}


.article-status-icon {
  color: #fff;
  font-size: 1.5rem;
  position: absolute;
  right: 1rem;
  bottom: 1.4rem;
}
</style>

