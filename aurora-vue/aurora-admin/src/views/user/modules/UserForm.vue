<template>
  <div>
    <form>
      <div>
        <label for="username">用户名：</label>
        <input type="text" id="username" v-model="user.username" required>
      </div>
      <div>
        <label for="email">邮箱：</label>
        <input type="email" id="email" v-model="user.email" required>
      </div>
      <div>
        <label for="avatar">头像：</label>
        <input type="file" id="avatar" @change="onFileChange">
        <img v-if="user.avatar" :src="user.avatar" alt="avatar">
      </div>
    </form>
  </div>
</template>

<script>
export default {
  name: "UserForm",
  data() {
    return {
      user: {
        username: '',
        email: '',
        avatar: null,
      },
    };
  },
  methods: {
    onFileChange(event) {
      this.user.avatar = URL.createObjectURL(event.target.files[0]);
    },

    addUser() {
      // 发送新增用户请求
      this.$emit('add-user', this.user);
      // 清空用户数据
      this.user.username = '';
      this.user.email = '';
      this.user.avatar = null;
    },

  },


};
</script>

<style scoped>

</style>
