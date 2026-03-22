<template>
	<div>
		<div class="breadcrumb-preview">
			<el-breadcrumb :separator="'≡'">
				<el-breadcrumb-item class="item1" to="/"><a>首页</a></el-breadcrumb-item>
				<el-breadcrumb-item class="item2" v-for="(item, index) in breadcrumbItem" :key="index"><a>{{item.name}}</a></el-breadcrumb-item>
			</el-breadcrumb>
		</div>
		<div v-if="centerType" class="back_box">
			<el-button class="backBtn" size="mini" @click="backClick">
				<span class="icon iconfont icon-jiantou33"></span>
				<span class="text">返回</span>
			</el-button>
		</div>
		<div class="list-preview">
			<div class="filter-row">
				<div class="search-toggle-btn" @click="showSearch = !showSearch">
					<span class="icon iconfont icon-search"></span>
					<span class="text">{{showSearch ? '收起查询' : '展开查询'}}</span>
				</div>
			</div>
			<el-form v-show="showSearch" :inline="true" :model="formSearch" class="list-form-pv">
				<el-form-item class="list-item">
					<div class="lable">商品分类：</div>
					<el-select v-model="curFenlei" placeholder="请选择分类" @change="fenleiChange" clearable>
						<el-option label="全部" value="全部"></el-option>
						<el-option v-for="(item, index) in fenlei" :key="index" :label="item[feileiColumn]" :value="item[feileiColumn]"></el-option>
					</el-select>
				</el-form-item>
				<el-form-item class="list-item">
					<div class="lable">商品名称：</div>
					<el-input v-model="formSearch.shangpinmingcheng" placeholder="商品名称" @keydown.enter.native="getList(1, curFenlei)" clearable></el-input>
				</el-form-item>
				<el-form-item class="list-item price-range">
				<div class="lable">价格：</div>
				<el-input v-model="formSearch.pricestart" placeholder="最小价格" clearable></el-input>
				<span class="price-separator">-</span>
				<el-input v-model="formSearch.priceend" placeholder="最大价格" class="price-end-input" clearable></el-input>
			</el-form-item>
				<el-button class="list-search-btn" v-if=" true " type="primary" @click="getList(1, curFenlei)">
					<i class="el-icon-search"></i>
					查询
				</el-button>
				<el-button class="list-add-btn" v-if="btnAuth('shangpinxinxi','新增')" type="primary" @click="add('/index/shangpinxinxiAdd')">
					<i class="el-icon-circle-plus-outline"></i>
					添加
				</el-button>
			</el-form>
			<div class="select2">
				<div class="select2-list" v-for="(item,index) in selectOptionsList" :key="item">
					<div class="label">{{item.name}}：</div>
					<div class="item-body">
						<div class="item" @click="selectClick2(item,-1)" :class="item.check ==-1 ? 'active' : ''">全部</div>
						<div class="item" @click="selectClick2(item,index1)" :class="item.check == index1 ? 'active' : ''" v-for="item1,index1 in item.list" :key="item1">{{item1}}</div>
					</div>
				</div>
			</div>
			<div class="sort_view">
				<el-button class="price-sort-btn" @click="sortClick('price')">
					<span class="icon iconfont icon-chujia13" v-if="sortType!='price'"></span>
					<span class="icon iconfont icon-jiantou23" v-else-if="sortType=='price'&&sortOrder=='desc'"></span>
					<span class="icon iconfont icon-jiantou24" v-else-if="sortType=='price'&&sortOrder=='asc'"></span>
					<span class="text">价格</span>
				</el-button>
				<el-button class="click-sort-btn" @click="sortClick('clicknum')">
					<span class="icon iconfont icon-liulan04" v-if="sortType!='clicknum'"></span>
					<span class="icon iconfont icon-jiantou23" v-else-if="sortType=='clicknum'&&sortOrder=='desc'"></span>
					<span class="icon iconfont icon-jiantou24" v-else-if="sortType=='clicknum'&&sortOrder=='asc'"></span>
					<span class="text">点击量：</span>
				</el-button>
				<el-button class="collect-sort-btn" @click="sortClick('storeupnum')">
					<span class="icon iconfont icon-shoucang10" v-if="sortType!='storeupnum'"></span>
					<span class="icon iconfont icon-jiantou23" v-else-if="sortType=='storeupnum'&&sortOrder=='desc'"></span>
					<span class="icon iconfont icon-jiantou24" v-else-if="sortType=='storeupnum'&&sortOrder=='asc'"></span>
					<span class="text">收藏数</span>
				</el-button>
			</div>
			<div class="list">
				<div class="list5">
					<div v-for="(item,index) in dataList" :key="index" class="list-item" @click.stop="toDetail(item)">
						<div class="imgbox">
							<img @click.stop="imgPreView(item.tupian)" v-if="item.tupian && item.tupian.substr(0,4)=='http'&&item.tupian.split(',w').length>1" :src="item.tupian" class="image" />
							<img @click.stop="imgPreView(item.tupian.split(',')[0])" v-else-if="item.tupian && item.tupian.substr(0,4)=='http'" :src="item.tupian.split(',')[0]" class="image" />
							<img @click.stop="imgPreView(baseUrl + (item.tupian?item.tupian.split(',')[0]:''))" v-else :src="baseUrl + (item.tupian?item.tupian.split(',')[0]:'')" class="image" />
						</div>
						<div class="infoBox">
							<div class="name">{{item.shangpinmingcheng}}</div>
							<div class="name">{{item.shangpinfenlei}}</div>
							<div class="price">￥<span class="price_text">{{item.price}}</span></div>
							<div class="bottomInfo">
								<div class="time_item">
									<span class="icon iconfont icon-shijian21"></span>
									<span class="label">发布时间：</span>
									<span class="text">{{item.addtime.split(' ')[0]}}</span>
								</div>
								<div class="publisher_item">
									<span class="icon iconfont icon-geren16"></span>
									<span class="label">发布人：</span>
									<span class="text">{{item.dianpumingcheng}}</span>
								</div>
								<div class="collect_item">
									<span class="icon iconfont icon-shoucang10"></span>
									<span class="label">收藏量：</span>
									<span class="text">{{item.storeupnum}}</span>
								</div>
								<div class="view_item">
									<span class="icon iconfont icon-liulan13"></span>
									<span class="label">点击量：</span>
									<span class="text">{{item.clicknum}}</span>
								</div>
							</div>
						</div>
					</div>
				</div>
			</div>

	
			<el-pagination
				background
				id="pagination"
				class="pagination"
				:pager-count="7"
				:page-size="pageSize"
				prev-text="<"
				next-text=">"
				:hide-on-single-page="false"
				:layout='["total","prev","pager","next","sizes","jumper"].join()'
				:total="total"
				:page-sizes="pageSizes"
				@current-change="curChange"
				@size-change="sizeChange"
				@prev-click="prevClick"
				@next-click="nextClick"
				></el-pagination>
		</div>
		<el-dialog title="预览图" :visible.sync="previewVisible" width="50%">
			<img :src="previewImg" alt="" style="width: 100%;">
		</el-dialog>
	</div>
</template>
<script>
	export default {
		//数据集合
		data() {
			return {
				selectIndex2: 0,
				selectOptionsList: [],
				layouts: '',
				swiperIndex: -1,
				baseUrl: '',
				breadcrumbItem: [
					{
						name: '商品信息'
					}
				],
				formSearch: {
					shangpinmingcheng: '',
					pinpai: '',
					price: '',
				},
				fenlei: [],
				feileiColumn: '',
				dataList: [],
				total: 1,
				pageSize: 12,
				pageSizes: [],
				totalPage: 1,
				curFenlei: '全部',
				isPlain: false,
				indexQueryCondition: '',
				pinpaiOptions: [],
				timeRange: [],
				centerType:false,
				previewImg: '',
				showSearch: false,
				previewVisible: false,
				sortType: 'id',
				sortOrder: 'desc',
			}
		},
		async created() {
			if(this.$route.query.centerType&&this.$route.query.centerType!=0){
				this.centerType = true
			}
			this.baseUrl = this.$config.baseUrl;
			await this.$http.get('option/pinpai/pinpai').then(res => {
				if (res.data.code == 0) {
					this.pinpaiOptions = res.data.data;
					this.selectOptionsList.push({name:'品牌',list:this.pinpaiOptions,tableName: 'pinpai',check: -1})
				}
			});
			await this.getFenlei();
			let fenlei = '全部'
			if(this.$route.query.homeFenlei){
				fenlei = this.$route.query.homeFenlei
			}
			this.getList(1, fenlei);
			// 页面加载时滚动到顶部
			window.scrollTo(0, 0);
		},
		watch:{
			$route(newValue){
				this.getList(1, newValue.query.homeFenlei);
			}
		},
		//方法集合
		methods: {
			fenleiChange(val) {
				if(!val) {
					this.curFenlei = '全部'
				}
				this.getList(1, this.curFenlei)
			},
			selectClick2(row,index) {
				row.check = index
				if(index == -1){
					this.formSearch[row.tableName] = ''
				}else {
					this.formSearch[row.tableName] = row.list[index]
				}
				this.getList()
			},
			add(path) {
				let query = {}
				if(this.centerType){
					query.centerType = 1
				}
				this.$router.push({path: path,query:query});
			},
			async getFenlei() {
				await this.$http.get('shangpinfenlei/list',{params: {sort: 'id',order: 'asc'}}).then(res => {
					if (res.data.code == 0) {
						this.fenlei = res.data.data.list
					}
				});
				this.feileiColumn = 'shangpinfenlei'
			},
			getList(page, fenlei, ref = '') {
				if(fenlei == '全部') this.swiperIndex = -1;
				for(let i=0;i<this.fenlei.length;i++) {
					if(fenlei == this.fenlei[i][this.feileiColumn]) {
						this.swiperIndex = i;
						break;
					}
				}
				if(fenlei){
					this.curFenlei = fenlei;
				}
				let params = {
					page,
					limit: this.pageSize,
					onshelves: 1
				};
				let searchWhere = {};
				if (this.formSearch.shangpinmingcheng != '') searchWhere.shangpinmingcheng = '%' + this.formSearch.shangpinmingcheng + '%';
				if (this.formSearch.pinpai != '') searchWhere.pinpai = this.formSearch.pinpai;
				if(this.formSearch.pricestart!='' && this.formSearch.pricestart!=undefined ){
					searchWhere.pricestart = this.formSearch.pricestart
				}
				if(this.formSearch.priceend!='' && this.formSearch.priceend!=undefined){
					searchWhere.priceend = this.formSearch.priceend
				}
				if (this.curFenlei != '全部') searchWhere.shangpinfenlei = this.curFenlei;
				let user = JSON.parse(localStorage.getItem('sessionForm'))
				if (this.sortType) searchWhere.sort = this.sortType
				if (this.sortOrder) searchWhere.order = this.sortOrder
				this.$http.get(`shangpinxinxi/${this.centerType?'page':'list'}`, {params: Object.assign(params, searchWhere)}).then(res => {
					if (res.data.code == 0) {
						this.dataList = res.data.data.list;
						this.total = Number(res.data.data.total);
						this.pageSize = Number(res.data.data.pageSize);
						this.totalPage = res.data.data.totalPage;
						if(this.pageSizes.length==0){
							this.pageSizes = [this.pageSize, this.pageSize*2, this.pageSize*3, this.pageSize*5];
						}
					}
				});
			},
			sortClick(type){
				if(this.sortType==type){
					if(this.sortOrder == 'desc'){
						this.sortOrder = 'asc'
					}else{
						this.sortOrder = 'desc'
					}
				}else{
					this.sortType = type
					this.sortOrder = 'desc'
				}
				this.getList(1, '全部')
			},
			curChange(page) {
				this.getList(page,this.curFenlei);
			},
			prevClick(page) {
				this.getList(page,this.curFenlei);
			},
			sizeChange(size){
				this.pageSize = size
				this.getList(1,this.curFenlei);
			},
			nextClick(page) {
				this.getList(page,this.curFenlei);
			},
			imgPreView(url){
				this.previewImg = url
				this.previewVisible = true
			},
			toDetail(item) {
				let params = {
					id: item.id
				}
				if(this.centerType){
					params.centerType = 1
				}
				this.$router.push({path: '/index/shangpinxinxiDetail', query: params});
			},
			btnAuth(tableName,key){
				if(this.centerType){
					return this.isBackAuth(tableName,key)
				}else{
					return this.isAuth(tableName,key)
				}
			},
			backClick() {
				this.$router.push({path: '/index/center'});
			},
		}
	}
</script>

<style rel="stylesheet/scss" lang="scss" scoped>
	.list-preview {
		margin: 0px auto;
		flex-direction: column;
		color: #333;
		background: none;
		display: flex;
		width: 1250px;
		font-size: 16px;
		justify-content: flex-start;
		align-items: flex-start;
		position: relative;
		flex-wrap: wrap;
		.category-3 {
			padding: 10px 0;
			margin: 0;
			background: #fff;
			display: flex;
			gap: 20px;
			width: 100%;
			flex-wrap: wrap;
			height: auto;
			.item {
				cursor: pointer;
				border-radius: 0;
				padding: 0;
				margin: 0;
				color: inherit;
				background: url(http://codegen.caihongy.cn/20241107/67909b0e3d9d4c95a6750d72379f0a31.webp) no-repeat center;
				display: flex;
				font-size: 16px;
				justify-content: center;
				align-items: center;
				min-width: 210px;
				height: 56px;
				img {
					border-radius: 100%;
					margin: 0 5px 0 0;
					object-fit: cover;
					display: block;
					width: 34px;
					height: 34px;
				}
				.text {
					color: inherit;
					font-size: inherit;
				}
			}
			.item:hover {
				color: #fff;
				background: url(http://codegen.caihongy.cn/20241107/67909b0e3d9d4c95a6750d72379f0a31.webp) no-repeat center;
			}
			.item.active {
				color: #fff;
				background: url(http://codegen.caihongy.cn/20241107/67909b0e3d9d4c95a6750d72379f0a31.webp) no-repeat center;
			}
		}
		.list-form-pv {
			padding: 0;
			margin: 20px 0;
			color: inherit;
			background: none;
			display: flex;
			width: 100%;
			font-size: inherit;
			flex-wrap: wrap;
			height: auto;
			.list-item {
				padding: 0;
				margin: 0 0px 10px 0;
				display: flex;
				font-size: inherit;
				align-items: center;
				flex-wrap: wrap;
				/deep/.el-form-item__content {
					display: flex;
					align-items: center;
				}
				.lable {
					padding: 0 10px;
					color: #9E9E9E;
					white-space: nowrap;
					display: inline-block;
					width: auto;
					font-size: 16px;
					line-height: 40px;
				}
				.el-input {
					width: auto;
				}
				&.price-range {
					/deep/.el-form-item__content {
						display: flex;
						align-items: center;
					}
					.price-separator {
						margin: 0 8px;
						color: #666;
					}
					.el-input {
						/deep/ .el-input__inner {
							width: 90px;
						}
					}
				}
				.datetimerange {
					border: 1px solid #ccc;
					border-radius: 8px;
					padding: 3px 3px;
					background: #fff;
					width: auto;
					justify-content: center;
				}
				.el-input /deep/ .el-input__inner {
					border: 1px solid #ccc;
					border-radius: 4px;
					padding: 0 10px;
					margin: 0 5px 0 0;
					color: #333;
					width: auto;
					font-size: 16px;
					line-height: 40px;
					height: 40px;
				}
				.el-select {
					width: 100%;
				}
				.el-select /deep/ .el-input__inner {
				}
				.el-date-editor {
					width: auto;
				}
				.el-date-editor /deep/ .el-input__inner {
					border: 1px solid #ccc;
					border-radius: 4px;
					padding: 0 0px 0 30px;
					margin: 0;
					color: #333;
					width: auto;
					font-size: 16px;
					line-height: 40px;
					height: 40px;
				}
			}
			.list-search-btn {
				cursor: pointer;
				border: 0;
				border-radius: 4px;
				padding: 0px 15px;
				margin: 0 10px 0 20px;
				color: #fff;
				background: #018cc0;
				width: auto;
				font-size: inherit;
				line-height: 40px;
				height: 40px;
				i {
					margin: 0 10px 0 0;
					color: #fff;
					font-size: inherit;
				}
			}
			.list-add-btn {
				cursor: pointer;
				border: 0;
				border-radius: 4px;
				padding: 0px 15px;
				margin: 0 10px 0 0;
				color: #fff;
				background: #03cce9;
				width: auto;
				font-size: inherit;
				line-height: 40px;
				height: 40px;
				i {
					margin: 0 10px 0 0;
					color: #fff;
					font-size: inherit;
				}
			}
		}
		.select2 {
			padding: 0;
			margin: 20px auto;
			background: none;
			width: 100%;
			font-size: 15px;
			height: auto;
			.select2-list {
				border: 1px solid #B2B2B2;
				border-radius: 5px;
				padding: 10px;
				margin: 0 0 10px;
				background: none;
				width: 100%;
				height: auto;
				.label {
					padding: 0 5px;
					color: #333;
					font-weight: 500;
					display: inline-block;
					font-size: inherit;
					line-height: 32px;
				}
				.item-body {
					display: inline-block;
					width: auto;
					flex-wrap: wrap;
					height: auto;
					.item {
						border-radius: 0;
						padding: 0 5px;
						color: inherit;
						background: none;
						display: inline-block;
						font-size: inherit;
						line-height: 32px;
						text-align: center;
						min-width: 50px;
					}
					.item:hover {
						cursor: pointer;
						color: #5DB5B7;
						background: none;
					}
					.item.active {
						cursor: pointer;
						color: #5DB5B7;
						background: none;
						display: inline-block;
						border-bottom: 2px solid #5DB5B7;
						text-align: center;
						min-width: 50px;
					}
				}
			}
		}
		.filter-row {
			padding: 10px 20px;
			display: flex;
			align-items: center;
			justify-content: center;
			background: #fff;
			border-bottom: 1px solid #eee;
			width: 100%;
			.search-toggle-btn {
				padding: 8px 16px;
				background: linear-gradient(135deg, #5DB5B7 0%, #3a9a9c 100%);
				color: #fff;
				border-radius: 16px;
				display: flex;
				align-items: center;
				justify-content: center;
				gap: 5px;
				cursor: pointer;
				font-size: 13px;
				transition: all 0.3s ease;
				box-shadow: 0 2px 8px rgba(93,181,183,0.3);
				.icon {
					font-size: 13px;
				}
			}
			.search-toggle-btn:hover {
				transform: translateY(-2px);
				box-shadow: 0 4px 12px rgba(93,181,183,0.4);
			}
		}
		.sort_view {
			padding: 5px 20px;
			margin: 0px auto;
			color: inherit;
			background: none;
			width: 100%;
			font-size: inherit;
			border: none;
			.price-sort-btn {
				border: 0;
				border-radius: 8px;
				padding: 0 5px;
				margin: 0 5px;
				color: inherit;
				background: none;
				font-size: inherit;
				.icon {
					margin: 0 2px 0 0;
					color: inherit;
					font-size: inherit;
					line-height: 40px;
				}
				.text {
					color: inherit;
					font-size: inherit;
					line-height: 40px;
				}
			}
			.click-sort-btn {
				border: 0;
				border-radius: 8px;
				padding: 0 5px;
				margin: 0 5px;
				color: inherit;
				background: none;
				font-size: inherit;
				.icon {
					margin: 0 2px 0 0;
					color: inherit;
					font-size: inherit;
					line-height: 40px;
				}
				.text {
					color: inherit;
					font-size: inherit;
					line-height: 40px;
				}
			}
			.collect-sort-btn {
				border: 0;
				border-radius: 8px;
				padding: 0 5px;
				margin: 0 5px;
				color: inherit;
				background: none;
				font-size: inherit;
				.icon {
					margin: 0 2px 0 0;
					color: inherit;
					font-size: inherit;
					line-height: 40px;
				}
				.text {
					color: inherit;
					font-size: inherit;
					line-height: 40px;
				}
			}
		}
		.list {
			margin: 20px auto;
			overflow: hidden;
			background: none;
			width: 100%;
			clear: both;
			font-size: 15px;
			.index-pv1 .animation-box {
				transform: rotate(0deg) scale(1) skew(0deg, 0deg) translate3d(0px, 0px, 0px);
				z-index: initial;
			}
				
			.index-pv1 .animation-box:hover {
				transform: rotate(0) scale(1.2) skew(0deg, 0deg) translate3d(0px, 0px, 0px);
				-webkit-perspective: 1000px;
				perspective: 1000px;
				transition: 0.3s;
				z-index: 1;
			}
				
			.index-pv1 .animation-box img {
				transform: rotate(0deg) scale(1) skew(0deg, 0deg) translate3d(0px, 0px, 0px);
			}
			
			.index-pv1 .animation-box img:hover {
				transform: rotate(0) scale(0.8) skew(0deg, 0deg) translate3d(0px, 0px, 0px);
				-webkit-perspective: 1000px;
				perspective: 1000px;
				transition: 0.3s;
			}
			.list5 {
				margin: 10px auto;
				align-content: flex-start;
				display: flex;
				width: 100%;
				align-items: flex-start;
				flex-wrap: wrap;
				.list-item {
					border: 1px solid #eee;
					padding: 10px;
					margin: 0 10px 20px;
					overflow: hidden;
					background: #fff;
					display: block;
					width: calc(25% - 20px);
					position: relative;
					.imgbox {
						margin: 0 auto;
						overflow: hidden;
						width: 200px;
						height: 200px;
						.image {
							filter: saturate(150%);
							border-radius: 0px;
							transform: rotate(0deg);
							object-fit: cover;
							display: block;
							width: 100%;
							opacity: 0.9;
							height: 100%;
						}
					}
					.infoBox {
						padding: 10px 10px;
						margin: 0 auto;
						left: 0px;
						bottom: 0;
						background: rgba(255, 255, 255, 0);
						width: 200px;
						font-size: 16px;
						position: inherit;
						transition: all 0.5s;
						.name {
							overflow: hidden;
							color: #5DB5B7;
							white-space: nowrap;
							font-weight: 600;
							width: 100%;
							font-size: 16px;
							line-height: 24px;
							text-overflow: ellipsis;
						}
						.price {
							color: #D90000;
							font-size: 14px;
							.price_text {
								color: #D90000;
								font-size: 18px;
							}
						}
						.bottomInfo {
							margin: 5px 0 0;
							flex-direction: column;
							color: #000000;
							display: flex;
							font-size: 15px;
							flex-wrap: wrap;
							.time_item {
								padding: 0;
								.icon {
									margin: 0 2px 0 0;
									color: #5FB6B8;
									font-size: inherit;
									line-height: 1.5;
								}
								.label {
									color: inherit;
									font-size: inherit;
									line-height: 1.5;
								}
								.text {
									color: inherit;
									font-size: inherit;
									line-height: 1.5;
								}
							}
							.publisher_item {
								padding: 0;
								margin: 0 10px 0 0;
								.icon {
									margin: 0 2px 0 0;
									color: #5FB6B8;
									font-size: inherit;
									line-height: 1.5;
								}
								.label {
									color: inherit;
									font-size: inherit;
									line-height: 1.5;
								}
								.text {
									color: inherit;
									font-size: inherit;
									line-height: 1.5;
								}
							}
							.like_item {
								padding: 0;
								margin: 0 10px 0 0;
								.icon {
									margin: 0 2px 0 0;
									color: #5FB6B8;
									font-size: inherit;
									line-height: 1.5;
								}
								.label {
									color: inherit;
									font-size: inherit;
									line-height: 1.5;
								}
								.text {
									color: inherit;
									font-size: inherit;
									line-height: 1.5;
								}
							}
							.collect_item {
								padding: 0;
								margin: 0 10px 0 0;
								.icon {
									margin: 0 2px 0 0;
									color: #5FB6B8;
									font-size: inherit;
									line-height: 1.5;
								}
								.label {
									color: inherit;
									font-size: inherit;
									line-height: 1.5;
								}
								.text {
									color: inherit;
									font-size: inherit;
									line-height: 1.5;
								}
							}
							.view_item {
								padding: 0;
								.icon {
									margin: 0 2px 0 0;
									color: #5FB6B8;
									font-size: inherit;
									line-height: 1.5;
								}
								.label {
									color: inherit;
									font-size: inherit;
									line-height: 1.5;
								}
								.text {
									color: inherit;
									font-size: inherit;
									line-height: 1.5;
								}
							}
						}
					}
				}
				.list-item:hover {
					border: 1px solid #03cce930;
					cursor: pointer;
					background: #03cce930;
					.imgbox {
						.image {
							transform: scale(1.05);
							ilter: saturate(100%);
							opacity: 1;
							transition: all 200ms linear;
						}
					}
					.infoBox {
						bottom: 0px;
						background: none;
						font-size: 16px;
						.name {
							color: #000;
						}
						.price {
							color: #000;
							.price_text {
							}
						}
						.bottomInfo {
							margin: 5px 0 0;
							flex-direction: column;
							color: #000000;
							display: flex;
							font-size: 15px;
							flex-wrap: wrap;
							.time_item {
								.icon {
									color: #000;
								}
								.label {
									color: #000;
								}
								.text {
									color: #000;
								}
							}
							.publisher_item {
								.icon {
									color: #000;
								}
								.label {
									color: #000;
								}
								.text {
									color: #000;
								}
							}
							.like_item {
								.icon {
									color: #000;
								}
								.label {
									color: #000;
								}
								.text {
									color: #000;
								}
							}
							.collect_item {
								.icon {
									color: #000;
								}
								.label {
									color: #000;
								}
								.text {
									color: #000;
								}
							}
							.view_item {
								.icon {
									color: #000;
								}
								.label {
									color: #000;
								}
								.text {
									color: #000;
								}
							}
						}
					}
				}
			}
		}
	}
</style>
