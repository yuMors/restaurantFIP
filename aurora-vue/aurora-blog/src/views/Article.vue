<template>
  <div class="flex flex-col">
    <div class="main-grid">
      <div class="post-header">
        <span class="post-labels">
          <ob-skeleton v-if="loading" tag="b" height="20px" width="35px"/>
          <b v-else-if="!loading && article.categoryName">
            <span>{{ article.categoryName }}</span>
          </b>
          <b v-else>{{ t('settings.default-category') }}</b>
          <ul>
            <ob-skeleton v-if="loading" :count="2" tag="li" height="16px" width="35px" class="mr-2"/>
            <template v-else-if="!loading && article.tags && article.tags.length > 0">
              <li v-for="tag in article.tags" :key="tag.id">
                <em class="opacity-50">#</em>
                {{ tag.tagName }}
              </li>
            </template>
            <template v-else>
              <li>
                <b class="opacity-50">#</b>
                {{ t('settings.default-tag') }}
              </li>
            </template>
          </ul>
        </span>
        <!-- 文章标题-->
        <h1 v-if="article.articleTitle" class="post-title text-white">
          {{ article.articleTitle }}
        </h1>
        <!-- ob-skeleton 组件是一个骨架屏组件，用于在数据加载之前，提供一个占位符，让用户知道该区域正在加载数据。
        该组件设置了 v-else 指令，表示当条件不成立时，也就是当数据加载完成后，隐藏该组件。-->
        <ob-skeleton
            v-else
            class="post-title text-white uppercase"
            width="100%"
            height="clamp(1.2rem, calc(1rem + 3.5vw), 4rem)"/>
        <div class="flex flex-row items-center justify-start mt-8 mb-4">
          <div class="post-footer" v-if="article.author">
            <img
                class="hover:opacity-50 cursor-pointer"
                v-lazy="article.author.avatar || ''"
                alt="author avatar"
                @click="handleAuthorClick(article.author.website)"/>
            <span class="text-white opacity-80">
              <strong
                  class="text-white pr-1.5 hover:opacity-50 cursor-pointer"
                  @click="handleAuthorClick(article.author.website)">
                {{ article.author.nickname }}
              </strong>
              <span class="opacity-70">
                {{ t('settings.shared-on') }} {{ t(`settings.months[${new Date(article.createTime).getMonth()}]`) }}
                {{ new Date(article.createTime).getDate() }}, {{ new Date(article.createTime).getFullYear() }}
              </span>
            </span>
          </div>
          <div class="post-footer" v-else>
            <div class="flex flex-row items-center">
              <ob-skeleton class="mr-2" height="28px" width="28px" :circle="true"/>
              <span class="text-ob-dim mt-1">
                <ob-skeleton height="20px" width="150px"/>
              </span>
            </div>
          </div>
          <div class="post-stats" v-if="wordNum !== '' && readTime !== ''">
            <span>
              <svg-icon icon-class="text-outline" style="stroke: white"/>
              <span class="pl-2 opacity-70">
                {{ wordNum }}
              </span>
            </span>
            <span>
              <svg-icon icon-class="clock-outline" style="stroke: white"/>
              <span class="pl-2 opacity-70">
                {{ readTime }}
              </span>
            </span>
          </div>
          <div v-else class="post-stats">
            <span>
              <svg-icon icon-class="clock"/>
              <span class="pl-2">
                <ob-skeleton width="40px" height="16px"/>
              </span>
            </span>
            <span>
              <svg-icon icon-class="text"/>
              <span class="pl-2">
                <ob-skeleton width="40px" height="16px"/>
              </span>
            </span>
          </div>
        </div>
      </div>
    </div>
    <div class="main-grid">
      <div>
        <!-- <template v-if="article.articleContent">
          <div class="post-html" ref="articleRef" v-html="article.articleContent"/>
        </template>
        <div v-else class="bg-ob-deep-800 px-14 py-16 rounded-2xl shadow-xl block min-h-screen">
          <ob-skeleton tag="div" :count="1" height="36px" width="150px" class="mb-6"/>
          <br/>
          <ob-skeleton tag="div" :count="35" height="16px" width="100px" class="mr-2"/>
          <br/>
          <br/>
          <ob-skeleton tag="div" :count="25" height="16px" width="100px" class="mr-2"/>
        </div> -->
        <!--这里就是上一篇和下一篇-->
        <div class="flex list">
          <!-- <div class="w-full h-full self-stretch mr-0 lg:mr-4" v-if="preArticleCard">
            <SubTitle title="settings.paginator.pre" icon="arrow-left-circle"/>
            <ArticleCard class="pre-and-next-article" :data="preArticleCard"/>
          </div> -->
          <div class="list-item-wrap" v-for="item in canteenList">
            <!-- <SubTitle title="settings.paginator.pre" icon="arrow-left-circle"/> -->
            <div class="list-item">
              <div class="item-left">
                <img :src="item.imgSrc" alt="">
              </div>
              <div class="divider"/>
              <div class="item-right">
                <span>{{ item.title }}</span>
                <p>价格：{{ `￥${item.price}` }}</p>
                <div class="desc">{{ item.desc }}</div>
              </div>
            </div>
          </div>

          <!-- <div class="w-full h-full self-stretch mt-0" v-if="nextArticleCard">
            <SubTitle title="settings.paginator.next" :side="!isMobile ? 'right' : 'left'" icon="arrow-right-circle"/>
            <ArticleCard class="pre-and-next-article" :data="nextArticleCard"/>
          </div> -->
        </div>
        <Comment/>
      </div>
      <div>
        <Sidebar>
          <Profile/>
          <Sticky :stickyTop="32" endingElId="footer" dynamicElClass="#sticky-sidebar">
            <div id="sticky-sidebar">
              <transition name="fade-slide-y" mode="out-in">
                <div class="sidebar-box mb-4">
                  <SubTitle :title="'titles.toc'" icon="toc"/>
                  <div id="toc1"></div>
                </div>
              </transition>
              <Navigator/>
            </div>
          </Sticky>
        </Sidebar>
      </div>
    </div>
  </div>
</template>

<script lang="ts">
import {Sidebar, Profile, Navigator} from '@/components/Sidebar'
import {
  computed,
  defineComponent,
  nextTick,
  onUnmounted,
  onMounted,
  reactive,
  ref,
  toRefs,
  provide,
  getCurrentInstance

} from 'vue'
import {useRoute, useRouter, onBeforeRouteUpdate} from 'vue-router'
import {useI18n} from 'vue-i18n'
import {Comment} from '@/components/Comment'
import {SubTitle} from '@/components/Title'
import {ArticleCard} from '@/components/ArticleCard'
import '@/styles/prism-aurora-future.css'
import {useCommonStore} from '@/stores/common'
import {useCommentStore} from '@/stores/comment'
import Sticky from '@/components/Sticky.vue'
import Prism from 'prismjs'
import tocbot from 'tocbot'
import emitter from '@/utils/mitt'
import {v3ImgPreviewFn} from 'v3-img-preview'
import api from '@/api/api'
import markdownToHtml from '@/utils/markdown'

export default defineComponent({
  name: 'Article',
  components: {Sidebar, Comment, SubTitle, ArticleCard, Profile, Sticky, Navigator},
  //setup 函数会在 beforeCreate 之后、created 之前执行
  setup() {
    const proxy: any = getCurrentInstance()?.appContext.config.globalProperties
    const commonStore = useCommonStore()
    const commentStore = useCommentStore()
    const route = useRoute()
    const router = useRouter()
    const {t} = useI18n()
    const loading = ref(true)
    const articleRef = ref()
    let md = require('markdown-it')()
    const canteenList: any = ref([]);
    let arr = ref([
      {
        title: '我是标题',
        imgSrc: 'https://img1.baidu.com/it/u=413643897,2296924942&fm=253&fmt=auto&app=138&f=JPEG?w=800&h=500',
        price: 20,
        desc: '简介简介简介简介简介简介简介简介简介简介简介简介简介简介简介简介'
      },
      {
        title: '我是标题222',
        imgSrc: 'https://img1.baidu.com/it/u=413643897,2296924942&fm=253&fmt=auto&app=138&f=JPEG?w=800&h=500',
        price: 20,
        desc: '简介2简介2简222介简介2简介2简介简介简介简222介简介简介22简介简介简222介简介简介'
      },
      {
        title: '我是标题33333',
        imgSrc: 'https://img1.baidu.com/it/u=413643897,2296924942&fm=253&fmt=auto&app=138&f=JPEG?w=800&h=500',
        price: 20,
        desc: '简介简介简介简介简介简介简介简介简介简介简介简介简介简介简介简介333333'
      },
    ]);
    const reactiveData = reactive({
      articleId: '' as any,
      article: '' as any,
      wordNum: '' as any,
      readTime: '' as any,
      comments: [] as any,
      images: [] as any,
      preArticleCard: '' as any,
      nextArticleCard: '' as any,
      haveMore: false as any,
      isReload: false as any
    })
    const pageInfo = reactive({
      current: 1,
      size: 7
    })
    commentStore.type = 1
    onMounted(() => {
      reactiveData.articleId = route.params.articleId
      toPageTop()
      fetchArticle()
      fetchComments()
      restaurantList();

      console.log(canteenList, 'canteenList');

    })
    onUnmounted(() => {
      commonStore.resetHeaderImage()
      reactiveData.article = ''
      tocbot.destroy()
    })
    onBeforeRouteUpdate((to) => {
      reactiveData.article = ''
      reactiveData.readTime = ''
      reactiveData.wordNum = ''
      reactiveData.comments = []
      reactiveData.images = []
      reactiveData.preArticleCard = ''
      reactiveData.nextArticleCard = ''
      reactiveData.articleId = to.params.articleId
      pageInfo.current = 1
      reactiveData.isReload = true
      toPageTop()
      fetchArticle()
      fetchComments()
      restaurantList()
    })
    provide(
        'comments',
        computed(() => reactiveData.comments)
    )
    provide(
        'haveMore',
        computed(() => reactiveData.haveMore)
    )
    provide(
        'canteenList',
        computed(() => canteenList)
    )
    emitter.on('articleFetchComment', () => {
      pageInfo.current = 1
      reactiveData.isReload = true
      fetchComments()
    })
    emitter.on('articleFetchReplies', (index) => {
      fetchReplies(index)
    })

    emitter.on('articleLoadMore', () => {
      fetchComments()
    })
    const handlePreview = (index: any) => {
      v3ImgPreviewFn({images: reactiveData.images, index: reactiveData.images.indexOf(index)})
    }
    const initTocbot = () => {
      console.log('-------------',articleRef)
      let nodes = articleRef.value.children
      if (nodes.length) {
        for (let i = 0; i < nodes.length; i++) {
          let node = nodes[i]
          let reg = /^H[1-4]{1}$/
          if (reg.exec(node.tagName)) {
            node.id = i
          }
        }
      }
      tocbot.init({
        tocSelector: '#toc1',
        contentSelector: '.post-html',
        headingSelector: 'h1, h2, h3',
        onClick: function (e) {
          e.preventDefault()
        }
      })
      const imgs = articleRef.value.getElementsByTagName('img')
      for (var i = 0; i < imgs.length; i++) {
        reactiveData.images.push(imgs[i].src)
        imgs[i].addEventListener('click', function (e: any) {
          handlePreview(e.target.currentSrc)
        })
      }
    }
    //获取餐厅列表 也就是菜谱
    const restaurantList = () => {
      //reactiveData.articleId 这个就是文章id
      console.log('reactiveData.articleId',reactiveData.articleId)
      api.getRestaurantList({articleId: reactiveData.articleId} ).then(({data}) => {
        if (data.flag) {
          canteenList.value = data.data || [];
        }
      })
    }
    //得到文章详情通过id 也就是餐厅
    const fetchArticle = () => {
      loading.value = true
      api.getArticeById(reactiveData.articleId).then(({data}) => {
        // if (data.code === 52003) {
        //   proxy.$notify({
        //     title: 'Error',
        //     message: '文章密码认证未通过',
        //     type: 'error'
        //   })
        //   router.push({path: '/出错啦'})
        //   return
        // }
        // if (data.data === null) {
        //   // router.push({path: '/出错啦'})
        //   return
        // }
        commonStore.setHeaderImage(data.data.articleCover)
        new Promise((resolve) => {
          data.data.articleContent = markdownToHtml(data.data.articleContent)
          resolve(data.data)
        }).then((article: any) => {
          reactiveData.article = article
          reactiveData.wordNum = Math.round(deleteHTMLTag(article.articleContent).length / 100) / 10 + 'k'
          reactiveData.readTime = Math.round(deleteHTMLTag(article.articleContent).length / 400) + 'mins'
          loading.value = false
          nextTick(() => {
            Prism.highlightAll()
            initTocbot()
          })
        })
        new Promise((resolve) => {
          data.data.preArticleCard.articleContent = md
              .render(data.data.preArticleCard.articleContent)
              .replace(/<\/?[^>]*>/g, '')
              .replace(/[|]*\n/, '')
              .replace(/&npsp;/gi, '')
          resolve(data.data.preArticleCard)
        }).then((preArticleCard: any) => {
          reactiveData.preArticleCard = preArticleCard
        })
        new Promise((resolve) => {
          data.data.nextArticleCard.articleContent = md
              .render(data.data.nextArticleCard.articleContent)
              .replace(/<\/?[^>]*>/g, '')
              .replace(/[|]*\n/, '')
              .replace(/&npsp;/gi, '')
          resolve(data.data.nextArticleCard)
        }).then((nextArticleCard) => {
          reactiveData.nextArticleCard = nextArticleCard
        })
      })
    }
    //查询评论
    const fetchComments = () => {
      const params = {
        type: 1,
        topicId: reactiveData.articleId,
        current: pageInfo.current,
        size: pageInfo.size
      }
      api.getComments(params).then(({data}) => {
        if (reactiveData.isReload) {
          reactiveData.comments = data.data.records
          reactiveData.isReload = false
        } else {
          reactiveData.comments.push(...data.data.records)
        }
        reactiveData.haveMore = data.data.count > reactiveData.comments.length;
        pageInfo.current++
      })
    }
    const fetchReplies = (index: any) => {
      api.getRepliesByCommentId(reactiveData.comments[index].id).then(({data}) => {
        reactiveData.comments[index].replyDTOs = data.data
      })
    }
    const handleAuthorClick = (link: string) => {
      if (link === '') link = window.location.href
      window.location.href = link
    }
    const toPageTop = () => {
      window.scrollTo({
        top: 0
      })
    }
    const deleteHTMLTag = (content: any) => {
      return content
          .replace(/<\/?[^>]*>/g, '')
          .replace(/[|]*\n/, '')
          .replace(/&npsp;/gi, '')
    }
    return {
      articleRef,
      ...toRefs(reactiveData),
      isMobile: computed(() => commonStore.isMobile),
      handleAuthorClick,
      loading,
      t,
      arr,
      canteenList,
    }
  },
})
</script>
<style lang="scss">
.flex .list {
  display: flex;
  flex-wrap: wrap;
  justify-content: space-between;

  .list-item-wrap {
    margin-right: 9px;
    margin-bottom: 25px;
  }

}

.flex .list .list-item {
  --tw-shadow: 0 10px 15px -3px rgba(0, 0, 0, 0.1), 0 4px 6px -2px rgba(0, 0, 0, 0.05);
  box-shadow: var(--tw-ring-offset-shadow, 0 0 #0000), var(--tw-ring-shadow, 0 0 #0000), var(--tw-shadow);
  z-index: 10;
  grid-template-rows: repeat(3, minmax(0, 1fr));
  transition: transform 0.2s ease-in-out;
  background-color: #FFF;
  border-radius: 12px;
  min-height: 274px;
  display: flex;
  justify-content: space-around;
  align-items: center;
  width: 531px;

  .divider {
    height: 200px;
    width: 1px;
    background-color: #c9c7c7;
  }

  .item-left {
    padding: 0 10px;

    img {
      width: 200px;
      height: 200px;
      background-color: aqua;
      border-radius: 8px;
    }
  }

  .item-right {
    display: flex;
    width: 259px;
    flex-direction: column;
    min-height: 190px;
    padding: 0 10px;


    span {
      font-size: 22px;
      white-space: nowrap;
      overflow: hidden;
    }

    p {
      font-size: 16px;
      margin: 20px 0 30px 0;
      white-space: nowrap;
      overflow: hidden;
    }

    .desc {
      display: -webkit-box;
      -webkit-line-clamp: 3;
      -webkit-box-orient: vertical;
      font-size: 15px;
      color: #c9c7c7;
      white-space: wrap;
      overflow: hidden;
      text-overflow: ellipsis;
      line-height: initial;
    }
  }
}

.post-html {
  word-wrap: break-word;
  word-break: break-all;
}

#toc1 > ol {
  list-style: none;
  counter-reset: li;
  padding-left: 1.5rem;

  > li {
    @apply font-medium pb-1;
    &.is-active-li > .node-name--H1 {
      @apply text-ob;
    }

    &.is-active-li > .node-name--H2 {
      @apply text-ob;
    }

    &.is-active-li > .node-name--H3 {
      @apply text-ob;
    }
  }

  ol li {
    @apply font-medium mt-1.5 mb-1.5;
    padding-left: 1.5rem;

    &.is-active-li > .node-name--H2 {
      @apply text-ob;
    }

    &.is-active-li > .node-name--H3 {
      @apply text-ob;
    }

    ol li {
      @apply font-medium mt-1.5 mb-1.5;
      padding-left: 1.5rem;

      &.is-active-li .node-name--H3 {
        @apply text-ob;
      }
    }
  }

  ol,
  ol ol {
    position: relative;
  }

  > li::before,
  ol > li::before,
  ol ol > li::before,
  ol ol ol > li::before,
  ol ol ol ol > li::before {
    content: '•';
    color: var(--text-accent);
    display: inline-block;
    width: 1em;
    margin-left: -1.15em;
    padding: 0;
    font-weight: medium;
    text-shadow: 0 0 0.5em var(--accent-2);
  }

  > li::before {
    @apply text-xl;
  }

  > li > ol::before,
  > li > ol > li > ol::before {
    content: '';
    border-left: 1px solid var(--text-accent);
    position: absolute;
    opacity: 0.35;
    left: -1em;
    top: 0;
    bottom: 0;
  }

  > li > ol::before {
    left: -1.25em;
    border-left: 2px solid var(--text-accent);
  }
}

.pre-and-next-article {
  .article-content {
    p {
      overflow: hidden;
      text-overflow: ellipsis;
      display: -webkit-box;
      -webkit-line-clamp: 5;
      -webkit-box-orient: vertical;
    }

    .article-footer {
      margin-top: 13px;
    }
  }
}
</style>
<style lang="scss" scoped>
.my-gap {
  gap: 1rem;
}
</style>
