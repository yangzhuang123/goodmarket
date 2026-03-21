const base = {
    get() {
        return {
            url : "http://localhost:8080/springboot48cmuaub/",
            name: "springboot48cmuaub",
            // 退出到首页链接
            indexUrl: 'http://localhost:8080/springboot48cmuaub/front/dist/index.html'
        };
    },
    getProjectName(){
        return {
            projectName: "基于SpringBoot的个性化网上商城系统的设计与实现"
        } 
    }
}
export default base
