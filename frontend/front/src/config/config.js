export default {
	baseUrl: 'http://localhost:8080/goodmarket/',
	name: '/goodmarket',
	indexNav: [
		{
			name: '商品信息',
			url: '/index/shangpinxinxi',
		},
		{
			name: '公告资讯',
			url: '/index/news'
		},
	],
	cateList: [
		{
			name: '商品信息',
			refTable: 'shangpinfenlei',
			refColumn: 'shangpinfenlei',
		},
		{
			name: '公告资讯',
			refTable: 'newstype',
			refColumn: 'typename',
		},
	]
}
