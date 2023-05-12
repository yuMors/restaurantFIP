<template>
  <el-card class="main-card">
    <div class="title">{{ this.$route.name }}</div>
    <br />
    <el-form :model="formData" ref="form" label-width="80px" :rules="rules">
      <el-form-item label="用户昵称" prop="nickname" style="margin-top: 2%;">
        <el-input v-model="formData.nickname" style="width: 30%" />
      </el-form-item>
      <el-form-item label="用户账号" prop="username">
        <el-input v-model="formData.username" placeholder="登录使用" style="width: 30%" />
      </el-form-item>
      <el-form-item label="用户密码" prop="password">
        <el-input v-model="formData.password" placeholder="可以不填，默认:123456" style="width: 30%" />
      </el-form-item>
      <el-form-item label="用户头像" prop="avatar">
        <!-- <el-upload  placeholder="默认:123456"
          action="/api/admin/articles/images"
          :show-file-list="false"
          :on-success="handleUploadSuccess"
          :on-error="handleUploadError"
          ref="upload"
          :limit="1"
          accept=".png, .jpg, .JPG, .PNG"
          :headers="headers">
          <el-button icon="el-icon-upload">用户头像</el-button>
        </el-upload> -->
        <el-upload class="avatar-uploader" action="/api/admin/articles/images" :show-file-list="false" :headers="headers"
          :on-success="handleAvatarSuccess" :before-upload="beforeAvatarUpload">
          <img v-if="avatar" :src="avatar" class="avatar">
          <i v-else class="el-icon-plus avatar-uploader-icon"></i>
        </el-upload>
      </el-form-item>
      <el-form-item>
        <el-button type="primary" @click="addUser">新增</el-button>
      </el-form-item>
    </el-form>

  </el-card>
</template>

<script>
import axios from 'axios';

export default {
  data() {
    return {
      headers: { Authorization: 'Bearer ' + sessionStorage.getItem('token') },
      formData: {
        nickname: '',
        username: '',
        password: '',
        avatar: null,
      },

      rules: {
        nickname: [
          //'trigger: "blur"' 是在 element-ui 的表单验证规则中用来指定验证触发方式的一种方式。
          // 在这种方式下，当表单控件失去焦点时就会触发该控件的验证。
          { required: true, message: '请输入用户名', trigger: 'blur' },
          { min: 1, max: 20, message: '长度在 1 到 20 个字符', trigger: 'blur' },
          { pattern: /^[^\s]*$/, message: '不能包含空格', trigger: 'blur'}
        ],
        username: [
          { required: true, message: '请输入账号', trigger: 'blur' },
          { pattern: /^[^\s]*$/, message: '不能包含空格', trigger: 'blur'}
          // { pattern: /^[0-9]+$/, message: '请输入数字', trigger: 'blur' }
        ],
        avatar: [
          { required: true, message: '请选择照片', trigger: 'blur' },
        ]
      },
    };

  },
  methods: {
    addUser() {
      this.$refs.form.validate(valid => {
        if (valid) {
          // 发送 POST 请求将表单数据上传到服务器
          this.axios.post('/api/user/add', this.formData)
            .then(response => {
              console.log(response);
              if(response.data.flag){
                this.$message.success('用户新增成功');
              }
              // 跳转到用户列表页面
              this.$router.push('/users');
            })
            .catch(error => {
              console.error(error);
            });
        } else {
          return false;
        }
      });
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
      this.$message.success('头像上传成功！');
    },
    handleUploadError(error, file, fileList) {
      console.error(error);
      this.$message.error('头像上传失败！');
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
};

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
</style>
