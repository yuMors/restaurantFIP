import { defineStore } from 'pinia'
//定义的文章的类的属性吧
export const useArticleStore = defineStore('articleStore', {
  state: () => {
    return {
      topArticle: '' as any,  //顶部1个
      featuredArticles: [] as any, //推荐2个
      articles: [] as any,
      categories: [] as any,
      archives: [] as any
    }
  },
  actions: {}
})
