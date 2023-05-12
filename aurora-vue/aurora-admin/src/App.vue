<template>
  <div id="app">
    <router-view />
  </div>
</template>

<script>
import { generaMenu } from '@/assets/js/menu'
export default {
  created() {
    if (this.$store.state.userInfo != null) {
      console.log('app.vue 拿到用户菜单： ')
      //拿到用户的菜单
      generaMenu()
    }
    //this.axios.post('/api/testApi')
  }
}
</script>
