const base = {
    get() {
        return {
            url : "http://localhost:8080/goodmarket/",
            name: "goodmarket",
            indexUrl: 'http://localhost:8080/goodmarket/front/dist/index.html'
        };
    },
    getProjectName(){
        return {
            projectName: "好物集市"
        } 
    }
}
export default base
