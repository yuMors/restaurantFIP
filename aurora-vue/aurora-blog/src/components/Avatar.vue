<template>
  <div class="flex-shrink-0">
    <div class="rounded-full ring-gray-100 overflow-hidden shaodw-lg w-9 xl:w-10">
      <template v-if="url != null"> <img class="avatar-img" :src="url" alt="" /></template>
      <template v-else><img class="avatar-img" :src="defaultImg" alt="" /></template>
    </div>
  </div>
</template>

<script lang="ts">
import { defineComponent, toRefs } from 'vue'

export default defineComponent({
  name: 'Avatar',
  props: ['url'],
  setup(props) {
    return {
      url: toRefs(props).url,
      // default: 'https://static.linhaojun.top/config/52a81cd2772167b645569342e81ce312.jpg'
      defaultImg: 'http://rs81ljuv5.hb-bkt.clouddn.com/2023/03/28/20230328-170921YH-d73d57c2a22ef2.png'
    }
  }
})
</script>
<style lang="scss" scoped>
.avatar-img {
  transition-property: transform;
  transition-timing-function: cubic-bezier(0.4, 0, 0.2, 1);
  transition-duration: 800ms;
  transform: rotate(-360deg);
}
.avatar-img:hover {
  transform: rotate(360deg);
}
</style>
