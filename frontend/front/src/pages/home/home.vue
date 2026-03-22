<template>
	<div class="home-preview">




		<!-- 系统简介 -->
		<div id="system" class="animate__animated">
			<div class="system_item">
				<div class="system_title_box">
					<div class="system_title">{{systemIntroductionDetail.title}}</div>
					<div class="system_subtitle">{{systemIntroductionDetail.subtitle}}</div>
				</div>
				<div class="system_img">
					<el-image :src="baseUrl + systemIntroductionDetail.picture1" :preview-src-list="[baseUrl + systemIntroductionDetail.picture1, baseUrl + systemIntroductionDetail.picture2, baseUrl + systemIntroductionDetail.picture3]" fit="cover" />
					<el-image :src="baseUrl + systemIntroductionDetail.picture2" :preview-src-list="[baseUrl + systemIntroductionDetail.picture1, baseUrl + systemIntroductionDetail.picture2, baseUrl + systemIntroductionDetail.picture3]" fit="cover" />
					<el-image :src="baseUrl + systemIntroductionDetail.picture3" :preview-src-list="[baseUrl + systemIntroductionDetail.picture1, baseUrl + systemIntroductionDetail.picture2, baseUrl + systemIntroductionDetail.picture3]" fit="cover" />
				</div>
				<div class="system_content ql-snow ql-editor" v-html="systemIntroductionDetail.content"></div>
				<div class="system_idea1" />
				<div class="system_idea2" />
				<div class="system_idea3" />
				<div class="system_idea4" />
				<div class="system_more" @click="toDetail('systemintroDetail',systemIntroductionDetail)">
					<span>查看更多</span>
					<span class="icon iconfont icon-gengduo1"></span>
				</div>
			</div>
		</div>
	<!-- 系统简介 -->
	<!-- 商品推荐 -->
		<div id="animate_recommendshangpinxinxi" class="recommend animate__animated">
			<div class="recommend_title_box">
				<span class="recommend_title">商品信息推荐</span>
				<span class="recommend_subhead">{{'shangpinxinxi'.toUpperCase()}} RECOMMEND</span>
			</div>
			<div v-if="shangpinxinxiRecommend.length" class="list list16 index-pv1">
				<div v-for="item,index in shangpinxinxiRecommend" :key="index"  @click="toDetail('shangpinxinxiDetail', item)" class="list-item animation-box">
					<div class="img" @click.stop>
						<el-image v-if="preHttp(item.tupian)" :src="item.tupian.split(',')[0]" :preview-src-list="[item.tupian.split(',')[0]]" fit="cover" />
						<el-image v-else :src="baseUrl + (item.tupian?item.tupian.split(',')[0]:'')" :preview-src-list="[baseUrl + (item.tupian?item.tupian.split(',')[0]:'')]" fit="cover" />
					</div>
					<div class="infoBox">
						<div class="info-left">
							<div class="name">{{item.shangpinmingcheng}}</div>
							<div class="name">{{item.shangpinfenlei}}</div>
							<div class="price">￥{{item.price}}</div>
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
								<span class="label">收藏：</span>
								<span class="text">{{item.storeupnum}}</span>
							</div>
							<div class="view_item">
								<span class="icon iconfont icon-chakan9"></span>
								<span class="label">浏览次数：</span>
								<span class="text">{{item.clicknum}}</span>
							</div>
						</div>
						<div class="desc ql-snow ql-editor" v-html="item.shangpincanshu"></div>
					</div>
				</div>
			</div>
			<div class="moreBtn" @click="moreBtn('shangpinxinxi')">
				<span class="text">查看更多</span>
				<i class="icon iconfont icon-gengduo1"></i>
			</div>
		</div>
		<!-- 商品推荐 -->
		<!-- 新闻资讯 -->
		<div id="animate_newsnews" class="news animate__animated">
			<div class="news_title_box">
				<span class="news_title">公告资讯</span>
				<span class="news_subhead">{{'news'.toUpperCase()}}</span>
			</div>
			<div class="list list11 index-pv1">
				<div class="left_box">
					<div class="list-item" v-for="(item,index) in newsList" :key="index" v-if="index<Number(2)" @click="toDetail('newsDetail', item)">
						<div class="img" @click.stop><el-image :src="baseUrl + item.picture" :preview-src-list="[baseUrl + item.picture]" fit="cover" /></div>
						<div class="infoBox">
							<div class="name">{{item.title}}</div>
							<div class="time">{{item.addtime}}</div>
							<div class="desc">{{ item.introduction }}</div>
						</div>
					</div>
				</div>
				<div class="right_box">
					<div class="list-item" :class="newsIndex11==index?'list-item-active':''" @click="newsTabClick11(index)"  v-for="(item,index) in newsList" :key="index" v-if="index>=Number(2)&&index<(Number(2) + Number(4))">
						<div class="name">{{item.title}}</div>
						<div class="list-item2" @click="toDetail('newsDetail', item)">
							<div class="desc">{{ item.introduction }}</div>
						</div>
					</div>
				</div>
			</div>
			<div class="moreBtn" @click="moreBtn('news')">
				<span class="text">查看更多</span>
				<i class="icon iconfont icon-gengduo1"></i>
			</div>
		</div>
		<!-- 新闻资讯 -->
	</div>
</template>

<script>
import 'animate.css'
import Swiper from "swiper";

	export default {
		//数据集合
		data() {
			return {
				baseUrl: '',
				systemIntroductionDetail: {},
				newsList: [],
				shangpinxinxiRecommend: [],


				newsIndex11: Number(2),



			}
		},
		created() {
			this.baseUrl = this.$config.baseUrl;
			this.getNewsList();
			this.getSystemIntroduction();
			this.getList();
		},
		mounted() {
			window.addEventListener('scroll', this.handleScroll)
			setTimeout(()=>{
				this.handleScroll()
			},100)
			
			this.swiperChanges()
		},
		beforeDestroy() {
			window.removeEventListener('scroll', this.handleScroll)
		},
		//方法集合
		methods: {
			newsTabClick11(index){
				this.newsIndex11 = index
			},
			swiperChanges() {
				setTimeout(()=>{
				},750)
			},


			handleScroll() {
				let arr = [
					{id:'about',css:'animate__'},
					{id:'system',css:'animate__'},
					{id:'animate_recommendshangpinxinxi',css:'animate__'},
					{id:'animate_newsnews',css:'animate__'},
				]
			
				for (let i in arr) {
					let doc = document.getElementById(arr[i].id)
					if (doc) {
						let top = doc.offsetTop
						let win_top = window.innerHeight + window.pageYOffset
						// console.log(top,win_top)
						if (win_top > top && doc.classList.value.indexOf(arr[i].css) < 0) {
							// console.log(doc)
							doc.classList.add(arr[i].css)
						}
					}
				}
			},
			preHttp(str) {
				return str && str.substr(0,4)=='http';
			},
			getSystemIntroduction() {
				this.$http.get('systemintro/detail/1', {}).then(res => {
					if(res.data.code == 0) {
						this.systemIntroductionDetail = res.data.data;
					}
				})
			},
			getNewsList() {
				let data = {
					page: 1,
					limit: 7,
					sort: 'addtime',
					order: 'desc'
				}
				this.$http.get('news/list', {params: data}).then(res => {
					if (res.data.code == 0) {
						this.newsList = res.data.data.list;
					
					}
				});
			},
			getList() {
				let autoSortUrl = "";
				let data = {}
				autoSortUrl = "shangpinxinxi/autoSort";
				if(localStorage.getItem('frontToken')) {
					autoSortUrl = "shangpinxinxi/autoSort2";
				}
				data = {
					page: 1,
					limit: 4,
					onshelves: 1,
				}
				this.$http.get(autoSortUrl, {params: data}).then(res => {
					if (res.data.code == 0) {
						this.shangpinxinxiRecommend = res.data.data.list;
					}
				});
			
			},
			toDetail(path, item) {
				this.$router.push({path: '/index/' + path, query: {id: item.id}});
			},
			moreBtn(path) {
				this.$router.push({path: '/index/' + path});
			}
		}
	}
</script>

<style rel="stylesheet/scss" lang="scss" scoped>
	.home-preview {
		margin: 0px auto;
		flex-direction: column;
		background: #f5f7fa;
		display: flex;
		width: 100%;
		#system {
			padding: 60px calc(50% - 625px);
			background: linear-gradient(135deg, #f8f9fa 0%, #e9ecef 100%);
			width: 100%;
			position: relative;
			height: auto;
			order: 3;
			.system_item {
				padding: 40px;
				margin: 0;
				z-index: 9;
				overflow: hidden;
				background: #fff;
				display: block;
				width: 100%;
				flex-wrap: wrap;
				border-radius: 16px;
				box-shadow: 0 4px 20px rgba(0,0,0,.08);
				.system_title_box {
					padding: 0;
					margin: 0 auto 30px;
					background: none;
					width: 100%;
					position: relative;
					text-align: center;
					height: auto;
					.system_title {
						padding: 0;
						margin: 0 0 10px;
						color: #333;
						background: none;
						display: inline-block;
						width: auto;
						font-size: 32px;
						font-weight: 600;
						line-height: inherit;
						height: auto;
					}
					.system_subtitle {
						margin: 0;
						color: #5DB5B7;
						display: block;
						width: 100%;
						font-size: 16px;
						line-height: 1.5;
						text-align: center;
						font-weight: 400;
					}
				}
				.system_img {
					padding: 0;
					margin: 20px auto;
					background: none;
					display: flex;
					gap: 20px;
					width: auto;
					align-items: center;
					justify-content: center;
					flex-wrap: wrap;
					height: auto;
					img, .el-image {
						margin: 0;
						object-fit: cover;
						display: block;
						width: 280px;
						height: 200px;
						border-radius: 12px;
						box-shadow: 0 4px 15px rgba(0,0,0,.1);
						transition: all 0.3s ease;
						cursor: pointer;
					}
					img:hover, .el-image:hover {
						transform: translateY(-5px);
						box-shadow: 0 8px 25px rgba(0,0,0,.15);
					}
				}
				.system_content {
					padding: 20px 0;
					margin: 20px 0;
					overflow: hidden;
					color: #666;
					background: none;
					width: 100%;
					font-size: 15px;
					line-height: 28px;
					height: auto;
					text-align: center;
				}
				.system_idea1, .system_idea2, .system_idea3, .system_idea4 {
					display: none;
				}
				.system_more {
					border: none;
					cursor: pointer;
					margin: 30px auto 0;
					background: linear-gradient(135deg, #5DB5B7 0%, #3a9a9c 100%);
					display: inline-block;
					width: auto;
					line-height: 44px;
					position: relative;
					padding: 0 30px;
					border-radius: 22px;
					text-align: center;
					transition: all 0.3s ease;
					box-shadow: 0 4px 15px rgba(93,181,183,.3);
					span:nth-child(1) {
						color: #fff;
						font-size: 15px;
						font-weight: 500;
					}
					span:nth-child(2) {
						color: #fff;
						display: inline-block;
						font-size: 15px;
						margin-left: 5px;
					}
				}
				.system_more:hover {
					transform: translateY(-2px);
					box-shadow: 0 6px 20px rgba(93,181,183,.4);
				}
			}
		}
		.news {
			padding: 60px calc(50% - 625px);
			margin: 0;
			background: linear-gradient(135deg, #f8f9fa 0%, #e9ecef 100%);
			width: 100%;
			position: relative;
			order: 1;
			.news_title_box {
				padding: 0;
				margin: 0 auto 40px;
				background: none;
				width: 100%;
				position: relative;
				text-align: center;
				height: auto;
				.news_title {
					padding: 0;
					margin: 0 0 10px;
					color: #333;
					background: none;
					display: inline-block;
					width: auto;
					font-size: 32px;
					font-weight: 600;
					line-height: inherit;
					height: auto;
				}
				.news_subhead {
					margin: 0;
					color: #5DB5B7;
					display: block;
					width: 100%;
					font-size: 14px;
					line-height: 1.5;
					text-align: center;
					font-weight: 400;
					letter-spacing: 2px;
				}
			}
			.index-pv1 .animation-box:hover {
				transform: translateY(-5px);
				transition: 0.3s;
				z-index: 1;
			}
			.index-pv1 .animation-box img:hover {
				transform: scale(1.05);
				transition: 0.3s;
			}
			.list11 {
				padding: 0;
				margin: 30px auto 0;
				overflow: hidden;
				background: none;
				display: flex;
				width: 100%;
				justify-content: space-between;
				gap: 30px;
				.left_box {
					width: 55%;
					display: grid;
					grid-template-columns: 1fr;
					gap: 20px;
					.list-item {
						border: none;
						cursor: pointer;
						padding: 0;
						margin: 0;
						overflow: hidden;
						display: flex;
						width: 100%;
						border-width: 0;
						justify-content: space-between;
						background: #fff;
						border-radius: 16px;
						overflow: hidden;
						transition: all 0.3s ease;
						box-shadow: 0 4px 15px rgba(0,0,0,.08);
						.img {
							flex: 0 0 auto;
							width: 220px;
							height: 180px;
							cursor: pointer;
							img, .el-image {
								object-fit: cover;
								width: 100%;
								transition: all 0.3s;
								height: 100%;
							}
						}
						.infoBox {
							padding: 25px;
							background: transparent;
							flex: 1 1 auto;
							width: 100%;
							display: flex;
							flex-direction: column;
							justify-content: center;
							.name {
								overflow: hidden;
								color: #333;
								white-space: nowrap;
								font-weight: 600;
								width: 100%;
								font-size: 18px;
								line-height: 28px;
								text-overflow: ellipsis;
							}
							.time {
								border-radius: 20px;
								margin: 12px 0;
								color: #fff;
								background: linear-gradient(135deg, #5DB5B7 0%, #3a9a9c 100%);
								width: fit-content;
								padding: 4px 15px;
								font-size: 12px;
								text-align: center;
								display: inline-block;
							}
							.desc {
								overflow: hidden;
								color: #666;
								line-height: 24px;
								height: 72px;
								font-size: 14px;
								display: -webkit-box;
								-webkit-line-clamp: 3;
								-webkit-box-orient: vertical;
							}
						}
					}
					.list-item:hover {
						transform: translateY(-5px);
						box-shadow: 0 8px 25px rgba(0,0,0,.12);
						.img {
							img {
								transform: scale(1.05);
							}
						}
						.infoBox {
							.name {
								color: #5DB5B7;
							}
						}
					}
				}
				.right_box {
					padding: 0;
					overflow: hidden;
					width: 42%;
					height: auto;
					min-height: 340px;
					background: #fff;
					border-radius: 16px;
					box-shadow: 0 4px 15px rgba(0,0,0,.08);
					padding: 20px;
					.list-item {
						position: relative;
						margin-bottom: 12px;
						.name {
							cursor: pointer;
							padding: 0 45px 0 20px;
							overflow: hidden;
							color: #333;
							white-space: nowrap;
							font-weight: 500;
							width: 100%;
							font-size: 15px;
							line-height: 48px;
							position: relative;
							text-overflow: ellipsis;
							background: #f8f9fa;
							border-radius: 10px;
							transition: all 0.3s ease;
						}
						.name::before {
							color: #5DB5B7;
							top: 0;
							font-weight: normal;
							font-size: 18px;
							line-height: 48px;
							position: absolute;
							right: 18px;
							content: "+";
							transition: all 0.3s ease;
						}
						.list-item2 {
							border: none;
							padding: 0 20px;
							margin: 0;
							overflow: hidden;
							display: block;
							opacity: 0;
							transition: all 0.3s;
							height: 0;
							background: #f8f9fa;
							border-radius: 0 0 10px 10px;
							.desc {
								cursor: pointer;
								margin: 10px 0;
								overflow: hidden;
								color: #666;
								font-size: 14px;
								line-height: 24px;
								height: auto;
								max-height: 100px;
							}
						}
					}
					.list-item.list-item-active {
						.name {
							color: #fff;
							background: linear-gradient(135deg, #5DB5B7 0%, #3a9a9c 100%);
							border-radius: 10px 10px 0 0;
						}
						.name::before {
							color: #fff;
							content: "-";
						}
						.list-item2 {
							padding: 10px 20px;
							margin: 0;
							display: block;
							opacity: 1;
							transition: all 0.3s;
							height: auto;
						}
					}
				}
			}
			.moreBtn {
				border: none;
				cursor: pointer;
				margin: 40px auto 0;
				background: linear-gradient(135deg, #5DB5B7 0%, #3a9a9c 100%);
				display: inline-block;
				width: auto;
				line-height: 44px;
				position: relative;
				padding: 0 30px;
				border-radius: 22px;
				text-align: center;
				transition: all 0.3s ease;
				box-shadow: 0 4px 15px rgba(93,181,183,.3);
				.text {
					color: #fff;
					font-size: 15px;
					font-weight: 500;
				}
				.icon {
					color: #fff;
					display: inline-block;
					font-size: 15px;
					margin-left: 5px;
				}
			}
			.moreBtn:hover {
				transform: translateY(-2px);
				box-shadow: 0 6px 20px rgba(93,181,183,.4);
			}
		}
		.recommend {
			padding: 60px calc(50% - 625px);
			margin: 0;
			background: linear-gradient(135deg, #f8f9fa 0%, #e9ecef 100%);
			width: 100%;
			position: relative;
			order: 2;
			.recommend_title_box {
				padding: 0;
				margin: 0 auto 40px;
				background: none;
				width: 100%;
				position: relative;
				text-align: center;
				height: auto;
				.recommend_title {
					padding: 0;
					margin: 0 0 10px;
					color: #333;
					background: none;
					display: inline-block;
					width: auto;
					font-size: 32px;
					font-weight: 600;
					line-height: inherit;
					height: auto;
				}
				.recommend_subhead {
					margin: 0;
					color: #5DB5B7;
					display: block;
					width: 100%;
					font-size: 14px;
					line-height: 1.5;
					text-align: center;
					font-weight: 400;
					letter-spacing: 2px;
				}
			}
			.index-pv1 .animation-box {
				transform: none;
				z-index: initial;
			}
			
			.index-pv1 .animation-box:hover {
				transform: translateY(-5px);
				transition: 0.3s;
				z-index: 1;
			}
			
			.index-pv1 .animation-box img {
				transform: none;
			}
			
			.index-pv1 .animation-box img:hover {
				transform: scale(1.05);
				transition: 0.3s;
			}
			.list16 {
				padding: 0;
				margin: 20px auto 0;
				color: #5DB5B7;
				background: none;
				display: grid;
				width: 100%;
				font-size: 14px;
				grid-template-columns: repeat(4, 1fr);
				gap: 25px;
				height: auto;
				.list-item {
					padding: 0;
					margin: 0;
					background: #fff;
					display: flex;
					flex-direction: column;
					width: 100%;
					position: relative;
					height: auto;
					border-radius: 16px;
					overflow: hidden;
					box-shadow: 0 4px 15px rgba(0,0,0,.08);
					transition: all 0.3s ease;
					.img {
						border: none;
						padding: 0;
						overflow: hidden;
						background: none;
						width: 100%;
						height: 200px;
						cursor: pointer;
						img, .el-image {
							object-fit: cover;
							display: block;
							width: 100%;
							height: 100%;
							transition: all 0.3s ease;
						}
					}
					.infoBox {
						padding: 20px;
						overflow: hidden;
						flex: 1;
						display: flex;
						flex-direction: column;
						justify-content: space-between;
						height: auto;
						.info-left {
							padding: 0;
							width: 100%;
							.name {
								padding: 0;
								overflow: hidden;
								color: #333;
								white-space: nowrap;
								width: 100%;
								font-size: 16px;
								font-weight: 600;
								line-height: 26px;
								text-overflow: ellipsis;
								margin-bottom: 5px;
							}
							.name:nth-child(2) {
								font-size: 13px;
								color: #888;
								font-weight: 400;
							}
							.price {
								padding: 0;
								color: #e74c3c;
								font-size: 20px;
								font-weight: 600;
								line-height: 1.8;
								margin: 8px 0;
							}
							.time_item, .publisher_item, .collect_item, .view_item {
								padding: 0;
								display: flex;
								align-items: center;
								margin-bottom: 4px;
								.icon {
									margin: 0 5px 0 0;
									display: inline-block;
									line-height: 20px;
									color: #5DB5B7;
									font-size: 12px;
								}
								.label {
									line-height: 20px;
									color: #999;
									font-size: 12px;
									margin-right: 4px;
								}
								.text {
									line-height: 20px;
									color: #666;
									font-size: 12px;
								}
							}
						}
						.desc {
							display: none;
						}
					}
				}
				.list-item:hover {
					transform: translateY(-8px);
					box-shadow: 0 12px 35px rgba(0,0,0,.15);
					.img img {
						transform: scale(1.08);
					}
				}
			}
			.moreBtn {
				border: none;
				cursor: pointer;
				margin: 40px auto 0;
				background: linear-gradient(135deg, #5DB5B7 0%, #3a9a9c 100%);
				display: inline-block;
				width: auto;
				line-height: 44px;
				position: relative;
				padding: 0 30px;
				border-radius: 22px;
				text-align: center;
				transition: all 0.3s ease;
				box-shadow: 0 4px 15px rgba(93,181,183,.3);
				.text {
					color: #fff;
					font-size: 15px;
					font-weight: 500;
				}
				.icon {
					color: #fff;
					display: inline-block;
					font-size: 15px;
					margin-left: 5px;
				}
			}
			.moreBtn:hover {
				transform: translateY(-2px);
				box-shadow: 0 6px 20px rgba(93,181,183,.4);
			}
		}
	}
</style>
