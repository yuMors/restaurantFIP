import { defineStore } from 'pinia'

export const useMetaStore = defineStore('metaStore', {
  state: () => {
    return {
      //这里定义窗口的名字 或悬页卡的名字 就是每一个标签页的名称
      title: '夜航'
    }
  }
})
