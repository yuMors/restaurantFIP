import axios from 'axios'
import {app} from '@/main'

axios.interceptors.request.use((config: any) => {
    config.headers['Authorization'] = 'Bearer ' + sessionStorage.getItem('token')
    return config
})

axios.interceptors.response.use(
    (response) => {
        switch (response.data.code) {
            case 50000:
                app.config.globalProperties.$notify({
                    title: 'Error',
                    message: '系统异常,请联系管理员',
                    type: 'error'
                })
                break
            case 40001:
                app.config.globalProperties.$notify({
                    title: 'Error',
                    message: '用户未登录',
                    type: 'error'
                })
                break
        }
        return response
    },
    (error) => {
        return Promise.reject(error)
    }
)
export default {
    //获取置顶和推荐文章
    getTopAndFeaturedArticles: () => {
        return axios.get('/api/articles/topAndFeatured')
    },
    //查询所有文章 home.vue 首页调用
    getArticles: (params: any) => {
        return axios.get('/api/articles/all', {params: params})
    },

    getArticlesByCategoryId: (params: any) => {
        return axios.get('/api/articles/categoryId', {params: params})
    },
    //获取餐厅列表
    getRestaurantList: (params: any) => {
        return axios.get('/api/restaurant/allList',{params: params})
    },
    //得到文章详情通过id 也就是餐厅 views/article.vue调用
    getArticeById: (articleId: any) => {
        return axios.get('/api/articles/' + articleId)
    },
    //获取所有分类
    getAllCategories: () => {
        return axios.get('/api/categories/all')
    },
    getAllTags: () => {
        return axios.get('/api/tags/all')
    },
    //获取前十个标签
    getTopTenTags: () => {
        return axios.get('/api/tags/topTen')
    },
    getArticlesByTagId: (params: any) => {
        return axios.get('/api/articles/tagId', {params: params})
    },
    getAllArchives: (params: any) => {
        return axios.get('/api/archives/all', {params: params})
    },
    login: (params: any) => {
        return axios.post('/api/users/login', params)
    },
    saveComment: (params: any) => {
        return axios.post('/api/comments/save', params)
    },
    getComments: (params: any) => {
        return axios.get('/api/comments', {params: params})
    },
    //获取前六个评论
    getTopSixComments: () => {
        return axios.get('/api/comments/topSix')
    },
    getAbout: () => {
        return axios.get('/api/about')
    },
    getFriendLink: () => {
        return axios.get('/api/links')
    },
    submitUserInfo: (params: any) => {
        return axios.put('/api/users/info', params)
    },
    getUserInfoById: (id: any) => {
        return axios.get('/api/users/info/' + id)
    },
    updateUserSubscribe: (params: any) => {
        return axios.put('/api/users/subscribe', params)
    },
    sendValidationCode: (username: any) => {
        return axios.get('/api/users/code', {
            params: {
                username: username
            }
        })
    },
    bindingEmail: (params: any) => {
        return axios.put('/api/users/email', params)
    },
    register: (params: any) => {
        return axios.post('/api/users/register', params)
    },
    searchArticles: (params: any) => {
        return axios.get('/api/articles/search', {
            params: params
        })
    },
    //获取相册列表 即将弃用
    // getAlbums: () => {
    //     return axios.get('/api/photos/albums')
    // },
    // getPhotosBuAlbumId: (albumId: any, params: any) => {
    //     return axios.get('/api/albums/' + albumId + '/photos', {
    //         params: params
    //     })
    // },
    //得到网站的配置信息
    getWebsiteConfig: () => {
        return axios.get('/api/getWebConfig')
    },
    qqLogin: (params: any) => {
        return axios.post('/api/users/oauth/qq', params)
    },
    //上报访客信息
    // report: () => {
    //   axios.post('/api/report')
    // },
    getTalks: (params: any) => {
        return axios.get('/api/talks', {
            params: params
        })
    },
    getTalkById: (id: any) => {
        return axios.get('/api/talks/' + id)
    },
    logout: () => {
        return axios.post('/api/users/logout')
    },
    getRepliesByCommentId: (commentId: any) => {
        return axios.get(`/api/comments/${commentId}/replies`)
    },
    updatePassword: (params: any) => {
        return axios.put('/api/users/password', params)
    },
    accessArticle: (params: any) => {
        return axios.post('/api/articles/access', params)
    }
}
