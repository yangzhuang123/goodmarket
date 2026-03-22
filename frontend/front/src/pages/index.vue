<template>
	<div class="main-containers">
		<div class="body-containers">
			<div class="top-container">
				<!-- info -->
				<div class="top_title">
					<span @click="goMenu('/index/home')">好物集市</span>
				</div>
				<div class="top_tel"></div>
			

				<el-dropdown class="dropdown-box" @command="handleCommand" trigger="click">
					<div class="el-dropdown-link" v-if="Token">
						<img class="top_avatar2" v-if="headportrait&&Token" :src="headportrait?baseUrl + headportrait:require('@/assets/avator.png')">
						<span class="top_label2"></span>
						<span class="top_nickname2">{{username}}</span>
						<span class="icon iconfont icon-xiala"></span>
					</div>
					<div class="el-dropdown-link" v-if="!Token">
						<div class="login-item" @click="toLogin">
							<span class="icon iconfont "></span>
							登录
						</div>
					</div>
					<el-dropdown-menu class="top-el-dropdown-menu" slot="dropdown" v-if="Token">
						<el-dropdown-item class="shop-item" :command="'shop'">
							<span class="icon iconfont icon-wuliu14"></span>
							购物车
						</el-dropdown-item>
						<el-dropdown-item class="service-item" :command="'service'">
							<span class="icon iconfont icon-touxiang04"></span>
							客服中心
						</el-dropdown-item>
						<el-dropdown-item v-if="notAdmin" class="user-item" :command="'user'">
							<span class="icon iconfont icon-touxiang02"></span>
							个人中心
						</el-dropdown-item>
						<el-dropdown-item class="register-item" :command="'register'">
							<span class="icon iconfont icon-fanhui21"></span>
							退出
						</el-dropdown-item>
					</el-dropdown-menu>
				</el-dropdown>
			</div>


			<div class="banner-preview" v-if="carouselChange() && showBanner">
				<div class="banner-close-btn" @click="showBanner = false">
					<span class="icon iconfont icon-guanbi1"></span>
				</div>
				<div class="swiper-container mySwiper3">
					<div class="swiper-wrapper">
						<div class="swiper-slide" v-for="item in carouselList" :key="item.id">
							<div class="swiper-item">
								<el-image v-if="preHttp(item.value)" @click="carouselClick(item.url)" :src="item.value" fit="cover"></el-image>
								<el-image v-else @click="carouselClick(item.url)" :src="baseUrl + item.value" fit="cover"></el-image>
							</div>
						</div>
					</div>
					<div class="banner-hidden">
					</div>
					<!-- Add Pagination -->
					<div class="swiper-pagination"></div>
					<!-- Add Arrows -->
					<div class="swiper-button-next">
						<span class="icon iconfont icon-jiantou18"></span>
					</div>
					<div class="swiper-button-prev">
						<span class="icon iconfont icon-jiantou39"></span>
					</div>
				</div>
			</div>
			<div class="banner-toggle-btn" v-if="!showBanner" @click="showBanner = true">
				<span class="icon iconfont icon-xiala"></span>
				<span class="text">展开轮播图</span>
			</div>
			<div class="menu-preview" v-if="isHomePage()">
				<div class="menu-list">
					<div class="menu-home" :class="activeMenu=='/index/home'?'menu-active':''" @click="goMenu('/index/home')">
						<div class="title">
							<span class="icon iconfont icon-home19"></span>
							<div class="text">系统首页</div>
						</div>
					</div>
					<div class="menu-item" v-for="(item,index) in menuList" :key="index" @mouseenter="menuShowClick4(index)" @mouseleave="menuShowClick4(-1)" :class="activeMenu==item.url?'menu-active':''" @click.stop="goMenu(item.url)">
						<div class="title">
							<span :class="iconArr[index]"></span>
							<div class="text">{{item.name}}</div>
						</div>
						<transition name="el-zoom-in-top">
							<div v-if="showType4==index&&item.hasCate" class="menu-child-list">
								<div class="child-item" v-for="(items,indexs) in item.cateList" :key="indexs" @click.stop="cateClick(item.url,items)">{{items}}</div>
							</div>
						</transition>
					</div>
					<div class="menu-shop" :class="activeMenu=='/index/cart'?'menu-active':''" @click="goMenu('/index/cart')" v-if="Token">
						<div class="title">
							<span class="icon iconfont icon-shouye-zhihui"></span>
							<div class="text">购物车</div>
						</div>
					</div>
					<div class="menu-service" @click="goChat" v-if="Token">
						<div class="title">
							<span class="icon iconfont icon-shouye-zhihui"></span>
							<div class="text">客服</div>
						</div>
					</div>
					<div class="menu-user" :class="activeMenu=='/index/center'?'menu-active':''" @click="goMenu('/index/center')" v-if="Token && notAdmin">
						<div class="title">
							<span class="icon iconfont icon-shouye-zhihui"></span>
							<div class="text">个人中心</div>
						</div>
					</div>
				</div>
			</div>
			<router-view id="scrollView" :class="{'has-top-margin': !isHomePage()}"></router-view>
			
			<div class="bottom-preview">
				<div class="footer"><div v-html="bottomContent"></div></div>
			</div>
		</div>
		
		<el-dialog title="客服中心" :visible.sync="chatFormVisible" width="600px" :before-close="chatClose">
			<div class="chat-content" id="chat-content">
				<div v-bind:key="item.id" v-for="item in chatList">
					<div v-if="item.addtime" style="width: 100%;text-align: center;font-size: 10px;color: #666;">{{timeFormat(item.addtime)}}</div>
					<div v-if="item.ask" class="right-content">
						<div style="display: flex;align-items: flex-start;">
							<el-alert v-if="item.type==1" class="text-content" :title="item.ask" :closable="false"
								type="warning"></el-alert>
							<el-image v-else-if="item.type==2" :src="baseUrl + item.ask" style="width: 150px;height: 150px;" fit="cover" :preview-src-list="[baseUrl + item.ask]"></el-image>
							<video v-else-if="item.type==3" :src="baseUrl + item.ask" style="width: 280px;" controls></video>
							<el-button v-else-if="item.type==4" type="primary" size="mini" @click="download(item.ask)">文件预览</el-button>
							<img :src="item.uimage?(baseUrl + item.uimage.split(',')[0]):require('@/assets/avator.png')" style="width: 30px;height: 30px;border-radius: 50%;margin: 0 0 0 5px;" alt="">
						</div>
					</div>
					<div v-else class="left-content">
						<div style="display: flex;align-items: flex-start;">
							<img :src="item.uimage?(baseUrl + item.uimage.split(',')[0]):require('@/assets/avator.png')" style="width: 30px;height: 30px;border-radius: 50%;margin: 0 5px 0 0;" alt="">
							<el-alert v-if="item.type==1" class="text-content" :title="item.reply" :closable="false"
								type="success"></el-alert>
							<el-image v-else-if="item.type==2" :src="baseUrl + item.reply" style="width: 150px;height: 150px;" fit="cover" :preview-src-list="[baseUrl + item.reply]"></el-image>
							<video v-else-if="item.type==3" :src="baseUrl + item.reply" style="width: 280px;" controls></video>
							<el-button v-else-if="item.type==4" type="primary" size="mini" @click="download(item.reply)">文件预览</el-button>
						</div>
					</div>
					<div class="clear-float"></div>
				</div>
			</div>
			<div slot="footer" class="dialog-footer">
				<div v-if="askShow"
					style="padding-bottom: 10px;display: flex;align-items: center;justify-content: center;">
					<el-upload class="upload-demo" :action="uploadUrl" :on-success="uploadSuccess" accept=".jpg,.png"
						:show-file-list="false">
						<el-button size="mini" type="success">上传图片</el-button>
					</el-upload>
					<el-upload class="upload-demo" :action="uploadUrl" :on-success="uploadSuccess2" accept=".mp4"
						:show-file-list="false">
						<el-button size="mini" type="success" style="margin: 0 0 0 10px;">上传视频</el-button>
					</el-upload>
					<el-upload class="upload-demo" :action="uploadUrl" :on-success="uploadSuccess3"
						:show-file-list="false">
						<el-button size="mini" type="success" style="margin: 0 0 0 10px;">上传文件</el-button>
					</el-upload>
					<el-button size="mini" type="primary" style="margin: 0 0 0 10px;" @click="askChange">
						转{{askType==1?'人工服务':'智能回复'}}</el-button>
				</div>
				<div style="width: 100%;display: flex;align-items: center;justify-content: space-between;">
					<img style="width: 30px;cursor: pointer;" @click="askShow = !askShow" src="../assets/jiahao.png">
					<el-input @keydown.enter.native="addChat(null)" v-model="form.ask" placeholder="请输入内容" style="width: calc(100% - 110px);">
					</el-input>
					<el-button type="primary" @click="addChat(null)">发送</el-button>
					<div style="position: relative;" v-if="askType==2">
						<span @click="showEmoji=!showEmoji" class="icon iconfont icon-gerenzhongxin-zhihui" style="font-size: 30px;color: #666;cursor: pointer;"></span>
						<picker
							:include="['people', 'Smileys']"
							:showSearch="false"
							:showPreview="false"
							:showCategories="false"
							@select="addEmoji"
							v-if="showEmoji"
							:backgroundImageFn="((set,sheetSize)=>{
								return require('@/assets/32.png')
							})"
							style="position: absolute;bottom: 40px;left: -100px;"
						/>
					</div>
				</div>
			</div>
		</el-dialog>
	</div>
</template>

<script>
	import Vue from 'vue'
	import Swiper from "swiper";
	import axios from 'axios'
	import { Picker } from "emoji-mart-vue";
	import timeMethod from '@/common/timeMethod'
	import {
		WebsocketMixin
	} from '@/mixins/WebsocketMixin'
export default {
	components:{
		Picker
	},
	mixins: [WebsocketMixin],
	data() {
		return {
			activeIndex: '0',
			baseUrl: '',
			carouselList: [],
			carouselForm: {
				inHome: true,
				inOther: true,
			},
			menuList: [],
			askType: 1, //1为智能回复 2为人工服务
			chatFormVisible: false,
			chatList: [],
			headers: {
				Token: localStorage.getItem('frontToken')
			},
			uploadUrl: this.$config.baseUrl + 'file/upload',
			askShow: false,
			showEmoji: false,
			form: {
				ask: '',
			},
			headportrait: localStorage.getItem('frontHeadportrait')?localStorage.getItem('frontHeadportrait'):'',
			Token: localStorage.getItem('frontToken'),
			username: localStorage.getItem('username'),
			notAdmin: localStorage.getItem('frontSessionTable')!='"users"',
			iconArr: [
				'el-icon-star-off',
				'el-icon-goods',
				'el-icon-warning',
				'el-icon-question',
				'el-icon-info',
				'el-icon-help',
				'el-icon-picture-outline-round',
				'el-icon-camera-solid',
				'el-icon-video-camera-solid',
				'el-icon-video-camera',
				'el-icon-bell',
				'el-icon-s-cooperation',
				'el-icon-s-order',
				'el-icon-s-platform',
				'el-icon-s-operation',
				'el-icon-s-promotion',
				'el-icon-s-release',
				'el-icon-s-ticket',
				'el-icon-s-management',
				'el-icon-s-open',
				'el-icon-s-shop',
				'el-icon-s-marketing',
				'el-icon-s-flag',
				'el-icon-s-comment',
				'el-icon-s-finance',
				'el-icon-s-claim',
				'el-icon-s-opportunity',
				'el-icon-s-data',
				'el-icon-s-check'
			],
			bottomContent: '',
			showType4: -1,
			showBanner: true,
		}
	},
	async created() {
		this.baseUrl = this.$config.baseUrl;
		this.menuList = this.$config.indexNav;
		this.getCarousel();
		if(localStorage.getItem('frontToken') && localStorage.getItem('frontToken')!=null) {
			this.getSession()
		}
		this.cateList = this.$config.cateList
		if(this.cateList.length){
			for(let x in this.menuList){
				for(let i in this.cateList){
					if(this.menuList[x].name==this.cateList[i].name){
						await this.$http.get(`option/${this.cateList[i].refTable}/${this.cateList[i].refColumn}`).then(rs=>{
							this.menuList[x].cateList = rs.data.data
							this.menuList[x].hasCate = true
						})
					}
				}
			}
		}
	},
	mounted() {
		this.activeIndex = localStorage.getItem('keyPath') || '0';


		// banner
		setTimeout(()=>{
			new Swiper(".mySwiper3", {"navigation":{"nextEl":".swiper-button-next","prevEl":".swiper-button-prev"},"autoplay":{"delay":2500,"disableOnInteraction":false},"pagination":{"el":".swiper-pagination","clickable":true}})
		}, 500)

	},
	computed: {
		activeMenu() {
			const route = this.$route
			const {
				meta,
				path
			} = route
			// if st path, the sidebar will highlight the path you sete
			if (meta.activeMenu) {
				return meta.activeMenu
			}
			return path
		},
	},
	watch: {
		$route(newValue) {
			let that = this
			let url = window.location.href
			let arr = url.split('#')
			for (let x in this.menuList) {
				if (newValue.path == this.menuList[x].url) {
					this.activeIndex = x
				}
			}
			this.Token = localStorage.getItem('frontToken')
			if(arr[1]!='/index/home'){
				window.scrollTo( 0, 70 )
			}else{
				window.scrollTo( 0, 0 )
			}
		},
		headportrait(){
			this.$forceUpdate()
		},
	},
	methods: {
		cateClick(url,fenlei){
			this.$router.push(url + '?homeFenlei=' + fenlei);
		},
		preHttp(str) {
			return str && str.substr(0,4)=='http';
		},

		async getSession() {
			await this.$http.get(`${localStorage.getItem('UserTableName')}/session`, {emulateJSON: true}).then(async res => {
				if (res.data.code == 0) {
					localStorage.setItem('sessionForm',JSON.stringify(res.data.data))
					localStorage.setItem('frontUserid', res.data.data.id);
					if(res.data.data.vip) {
						localStorage.setItem('vip', res.data.data.vip);
					}
					if(res.data.data.touxiang) {
						this.headportrait = res.data.data.touxiang
						localStorage.setItem('frontHeadportrait', res.data.data.touxiang);
					} else if(res.data.data.headportrait) {
						this.headportrait = res.data.data.headportrait
						localStorage.setItem('frontHeadportrait', res.data.data.headportrait);
					}
				}
			});
		},
		handleSelect(keyPath) {
			if (keyPath) {
				localStorage.setItem('keyPath', keyPath)
			}
		},
		toLogin() {
		  this.$router.push('/login');
		},
		logout() {
			localStorage.clear();
			Vue.http.headers.common['Token'] = "";
			this.$router.push('/index/home');
			this.activeIndex = '0'
			localStorage.setItem('keyPath', this.activeIndex)
			this.Token = ''
			this.$forceUpdate()
			this.$message({
				message: '登出成功',
				type: 'success',
				duration: 1000,
			});
		},
		getCarousel() {
			this.$http.get('config/list', {params: { page: 1, limit: 3 }}).then(res => {
				if (res.data.code == 0) {
					this.carouselList = res.data.data.list;
				}
			});
		},
		// 轮播图跳转
		carouselClick(url) {
			if (url) {
				if (url.indexOf('https') != -1) {
					window.open(url)
				} else {
					this.$router.push(url)
				}
			}
		},
		carouselChange(){
			let url = window.location.href
			let arr = url.split('#')
			// 只有系统首页展示轮播图
			return arr[1]=='/index/home'
		},
		isHomePage(){
			let url = window.location.href
			let arr = url.split('#')
			// 只在系统首页展示菜单导航栏
			return arr[1]=='/index/home'
		},
		goBackend() {
			localStorage.setItem('Token', localStorage.getItem('frontToken'));
			localStorage.setItem('role', localStorage.getItem('frontRole'));
			localStorage.setItem('sessionTable', localStorage.getItem('frontSessionTable'));
			localStorage.setItem('headportrait', localStorage.getItem('frontHeadportrait'));
			localStorage.setItem('userid', localStorage.getItem('frontUserid'));
			window.location.href = `${this.$config.baseUrl}admin/dist/index.html`
			
		},
		formatMessages(messages) {
			let lastTime = null;
			messages.forEach((message, index) => {
				const currentTime = new Date(message.addtime).getTime();
				if (lastTime !== null) {
					const timeDiff = (currentTime - lastTime) / 1000 / 60; // 转换为分钟
					if (timeDiff < 3) {
						message.addtime = ''; // 如果小于3分钟，不显示时间
					}
				}
				lastTime = currentTime;
			});
			return messages;
		},
		timeFormat(time) {
			const Time = timeMethod.getTime(time).split("T");
			//当前消息日期属于周
			const week = timeMethod.getDateToWeek(time);
			//当前日期0时
			const nti = timeMethod.setTimeZero(timeMethod.getNowTime());
			//消息日期当天0时
			const mnti = timeMethod.setTimeZero(timeMethod.getTime(time));
			//计算日期差值
			const diffDate = timeMethod.calculateTime(nti, mnti);
			//本周一日期0时 （后面+1是去除当天时间）
			const fwnti = timeMethod.setTimeZero(timeMethod.countDateStr(-timeMethod.getDateToWeek(timeMethod
				.getNowTime()).weekID + 1));
			//计算周日期差值
			const diffWeek = timeMethod.calculateTime(mnti, fwnti);
		
			if (diffDate === 0) { //消息发送日期减去当天日期如果等于0则是当天时间
				return Time[1].slice(0, 5);
			} else if (diffDate < 172800000) { //当前日期减去消息发送日期小于2天（172800000ms）则是昨天-  一天最大差值前天凌晨00:00:00到今天晚上23:59:59
				return "昨天 " + Time[1].slice(0, 5);
			} else if (diffWeek >= 0) { //消息日期减去本周一日期大于0则是本周
				return week.weekName;
			} else { //其他时间则是日期
				return Time[0].slice(5, 10);
			}
		},
		addEmoji(e) {
			this.form.ask += e.native;
			this.showEmoji = false
		},
		getChatList() {
			this.$http.get('chat/list', {params: { userid: Number(localStorage.getItem('frontUserid')), sort: 'addtime', order: 'asc',limit: 1000 }}).then(res => {
				if (res.data.code == 0) {
					this.chatList = this.formatMessages(res.data.data.list);
					let div = document.getElementsByClassName('chat-content')[0]
					setTimeout(() => {
						if (div){
							div.scrollTop = div.scrollHeight
						}
					}, 0)
				}
			});
		},
		addChat(ask=null,type=1) {
			let params = JSON.parse(JSON.stringify(this.form))
			if(params.ask==''&&ask==null){
				this.$message.error('内容不能为空')
				return false
			}
			if(ask){
				params.ask = ask
			}
			params.type = type
			params.uimage = localStorage.getItem('frontHeadportrait')
			params.uname = localStorage.getItem('username')
			params.userid = Number(localStorage.getItem('frontUserid'))
			this.$http.post('chat/add', params).then(res => {
				if (res.data.code == 0) {
					if (this.askType == 1 && ask == null) {
						let ask2 = JSON.parse(JSON.stringify(this.form.ask))
						this.$nextTick(function() {
							setTimeout(() => {
								this.getChathelper(ask2)
							}, 1000) // 要加点延迟, 不然有可能不生效
						})
					}
					if(this.askType==2){
						this.websocketSend(ask?ask:params.ask)
					}
					this.form.ask = '';
					this.getChatList();
				}
			});
		},
		chatClose() {
			if(this.askType==2){
				this.websocketOnclose();
			}
			this.chatFormVisible = false;
		},
		websocketOnmessage:function(e) {
			this.getChatList()
		},
		goChat() {
			if(!localStorage.getItem('frontToken')) {
				this.toLogin();
				return;
			}
			this.askType = 1
			this.saveChathelper('主人，我是您的智能助手小搏，请问有什么可以帮您！');
			this.getChatList();
			this.chatFormVisible = true;
		},
		uploadSuccess(res) {
			if (res.code == 0) {
				this.askShow = !this.askShow;
				this.addChat('upload/' + res.file,2)
			}
		},
		uploadSuccess2(res) {
			if (res.code == 0) {
				this.askShow = !this.askShow;
				this.addChat('upload/' + res.file,3)
			}
		},
		uploadSuccess3(res) {
			if (res.code == 0) {
				this.askShow = !this.askShow;
				this.addChat('upload/' + res.file,4)
			}
		},
		download(url){
			if(!url){
				return false
			}
			window.open((location.href.split(this.$config.name).length>1 ? location.href.split(this.$config.name)[0] + this.$config.name + '/' + url :this.$config.baseUrl + url))
		},
		getChathelper(ask) {
			this.$http.get('chathelper/page', {
				params: {
					ask: `%${ask}%`,
					limit: 100
				}
			}).then(res => {
				if (res.data.code == 0) {
					if (res.data.data.list.length) {
						let arr = this.getRandomNumber(res.data.data.list)
						this.saveChathelper(arr.reply)
					} else {
						this.saveChathelper('主人，小搏还不够聪明，无法理解您的意思！')
					}
				}
			});
		},
		getRandomNumber(arr) {
			var randomIndex = Math.floor(Math.random() * arr.length);
			return arr[randomIndex];
		},
		saveChathelper(reply) {
			this.$http.post('chat/save', {
				reply: reply,
				userid: Number(localStorage.getItem('frontUserid')),
				type: 1
			}).then(res => {
				if (res.data.code == 0) {
					this.form.ask = '';
					this.getChatList();
				}
			});
		},
		askChange() {
			this.askShow = !this.askShow;
			this.askType = this.askType == 1 ? 2 : 1
			if(this.askType==1) {
				this.saveChathelper('主人，我是您的智能助手小搏，请问有什么可以帮您！');
				this.websocketOnclose();
			} else if(this.askType==2){
				this.saveChathelper('您好，在线客服很高兴为您服务！');
				this.initWebSocket(1)
			}
		},
		menuShowClick4(index){
			this.showType4 = index
		},
		goMenu(path) {
			this.$router.push(path);
		},
		handleCommand(name){
			if(name == 'register') {
				this.logout()
			}
			else if (name == 'shop') {
				this.goMenu('/index/cart')
			}
			else if (name == 'service') {
				this.goChat()
			}
			else if (name == 'user'){
				this.goMenu('/index/center')
			}
			else if (name == 'login'){
				this.toLogin()
			}
		},
	}
}
</script>

<style rel="stylesheet/scss" lang="scss" scoped>
	.top-el-dropdown-menu {
		border: 1px solid #EBEEF5;
		border-radius: 8px;
		padding: 8px 0;
		box-shadow: 0 4px 20px rgba(0,0,0,.12);
		margin: 18px 0;
		background: #fff;
		.shop-item, .service-item, .user-item, .register-item {
			border: 0;
			padding: 0 16px;
			margin: 0;
			color: #333;
			background: #fff;
			width: auto;
			font-size: 14px;
			line-height: 40px;
			height: 40px;
			transition: all 0.3s ease;
			.icon {
				color: inherit;
				font-size: inherit;
				margin-right: 8px;
			}
		}
		.shop-item:hover, .service-item:hover, .user-item:hover, .register-item:hover {
			color: #fff;
			background: linear-gradient(135deg, #5DB5B7 0%, #3a9a9c 100%);
		}
	}
	.main-containers {
		.body-containers {
			padding: 0;
			margin: 0;
			background: #f5f7fa;
			min-height: 100vh;
			position: relative;
			.top-container {
				padding: 0 60px;
				z-index: 1002;
				color: #fff;
				display: flex;
				font-size: 16px;
				border-bottom: none;
				box-shadow: 0 2px 20px rgba(0,0,0,.1);
				background: linear-gradient(135deg, #5DB5B7 0%, #3a9a9c 100%);
				width: 100%;
				justify-content: space-between;
				align-items: center;
				position: fixed;
				top: 0;
				left: 0;
				height: 70px;
				.top_title {
					flex: 1 1 auto;
					position: relative;
					order: 1;
					span {
						padding: 0;
						color: #fff;
						font-size: 26px;
						font-weight: 600;
						line-height: 70px;
						cursor: pointer;
						letter-spacing: 2px;
						text-shadow: 0 2px 4px rgba(0,0,0,.1);
					}
				}
				.top_tel {
					margin: 0 10px;
					color: #fff;
					font-size: 16px;
				}
				.dropdown-box {
					color: inherit;
					display: flex;
					font-size: inherit;
					right: 0;
					order: 4;
					.el-dropdown-link {
						color: inherit;
						display: flex;
						font-size: inherit;
						align-items: center;
						.top_avatar2 {
							border-radius: 50%;
							margin: 0 10px;
							object-fit: cover;
							display: inline-block;
							width: 40px;
							height: 40px;
							border: 2px solid rgba(255,255,255,0.5);
							box-shadow: 0 2px 8px rgba(0,0,0,.15);
						}
						.top_label2 {
							color: inherit;
							font-size: inherit;
							line-height: 32px;
						}
						.top_nickname2 {
							color: #fff;
							font-size: 15px;
							line-height: 32px;
							font-weight: 500;
						}
						.icon {
							margin: 0 0 0 5px;
							color: rgba(255,255,255,0.8);
							font-size: 14px;
						}
						.login-item {
							border: none;
							padding: 0 24px;
							margin: 0;
							color: #5DB5B7;
							background: #fff;
							width: auto;
							font-size: 15px;
							font-weight: 500;
							line-height: 40px;
							height: 40px;
							border-radius: 20px;
							cursor: pointer;
							transition: all 0.3s ease;
							box-shadow: 0 2px 8px rgba(0,0,0,.1);
						}
						.login-item:hover {
							color: #fff;
							background: rgba(255,255,255,0.2);
							transform: translateY(-2px);
							box-shadow: 0 4px 12px rgba(0,0,0,.15);
						}
					}
				}
			}
			.menu-preview {
				.el-scrollbar {
					height: 100%;
				
					& /deep/ .scrollbar-wrapper-vertical {
						overflow-x: hidden;
					}
				
					& /deep/ .scrollbar-wrapper-horizontal {
						overflow-y: hidden;
				
						.el-scrollbar__view {
							white-space: nowrap;
						}
					}
				}
				padding: 0;
				margin: 0;
				background: #fff;
				width: 100%;
				border-color: transparent;
				border-width: 0;
				border-style: solid;
				box-shadow: 0 2px 10px rgba(0,0,0,.05);
				.menu-list {
					padding: 0 60px;
					display: flex;
					width: 100%;
					justify-content: center;
					position: relative;
					flex-wrap: wrap;
					.menu-home, .menu-item {
						color: #333;
						display: flex;
						align-items: center;
						height: 60px;
						transition: all 0.3s ease;
						.title {
							cursor: pointer;
							padding: 0 25px;
							color: #333;
							display: flex;
							align-items: center;
							.icon {
								padding: 0 8px 0 0;
								margin: 0;
								color: inherit;
								display: none;
								width: 14px;
								font-size: 14px;
								line-height: 50px;
								height: 50px;
							}
							.text {
								padding: 0;
								color: inherit;
								font-size: 16px;
								font-weight: 500;
								line-height: 60px;
								height: 60px;
								position: relative;
							}
						}
					}
					.menu-home:hover, .menu-item:hover {
						.title {
							color: #5DB5B7;
							background: none;
							.text::after {
								content: '';
								position: absolute;
								bottom: 0;
								left: 50%;
								transform: translateX(-50%);
								width: 30px;
								height: 3px;
								background: #5DB5B7;
								border-radius: 2px;
							}
						}
					}
					.menu-home.menu-active, .menu-item.menu-active {
						.title {
							color: #5DB5B7;
							background: none;
							.text::after {
								content: '';
								position: absolute;
								bottom: 0;
								left: 50%;
								transform: translateX(-50%);
								width: 30px;
								height: 3px;
								background: #5DB5B7;
								border-radius: 2px;
							}
						}
					}
					.menu-item {
						.title {
							border-left: none;
						}
						.menu-child-list {
							border: none;
							padding: 10px 0;
							z-index: 11;
							left: 0;
							background: #fff;
							bottom: -70px;
							display: flex;
							width: 100%;
							line-height: 50px;
							justify-content: center;
							position: absolute;
							height: auto;
							box-shadow: 0 4px 20px rgba(0,0,0,.1);
							border-radius: 0 0 8px 8px;
							.child-item {
								cursor: pointer;
								padding: 0 20px;
								color: #333;
								font-size: 14px;
								transition: all 0.3s ease;
							}
							.child-item:hover {
								color: #5DB5B7;
								background: none;
							}
						}
					}
					.menu-shop, .menu-service, .menu-user {
						cursor: pointer;
						color: #333;
						display: none;
						line-height: 50px;
						height: 50px;
						.title {
							padding: 0 20px;
							display: flex;
							height: 50px;
							.icon {
								padding: 0 10px;
								margin: 0;
								color: inherit;
								width: 14px;
								font-size: 14px;
								line-height: 50px;
								height: 50px;
							}
							.text {
								padding: 0 10px;
								color: inherit;
								font-size: 14px;
								line-height: 50px;
								height: 50px;
							}
						}
					}
				}
			}
			.banner-preview {
				margin: 0;
				width: 100%;
				max-width: 100%;
				padding: 0;
				height: auto;
				position: relative;
				.banner-close-btn {
					position: absolute;
					top: 20px;
					right: 20px;
					z-index: 1000;
					width: 40px;
					height: 40px;
					background: rgba(0,0,0,0.5);
					border-radius: 50%;
					display: flex;
					align-items: center;
					justify-content: center;
					cursor: pointer;
					transition: all 0.3s ease;
					.icon {
						color: #fff;
						font-size: 20px;
					}
				}
				.banner-close-btn:hover {
					background: rgba(0,0,0,0.7);
					transform: scale(1.1);
				}
				.swiper-container {
					border-radius: 0;
					overflow: hidden;
					box-shadow: none;
					width: 100%;
				}
				.swiper-button-prev:after {
					display:none;
				}
				.swiper-button-next:after {
					display:none;
				}
				.swiper-slide {
					.swiper-item {
						width: 100%;
						height: auto;
						.el-image {
							object-fit: cover;
							width: 100%;
							height: 500px;
							display: block;
						}
					}
				}
				@keyframes wave1 {from { left: -236px } to { left: -1233px }}
				@keyframes wave2 {from { left: 0 } to { left: -1009px }}
				.swiper-pagination {
					left: 0;
					bottom: 20px;
					width: 100%;
					/deep/ span.swiper-pagination-bullet {
						border-radius: 100%;
						margin: 0 6px;
						background: #fff;
						display: inline-block;
						width: 10px;
						opacity: .5;
						height: 10px;
						transition: all 0.3s ease;
					}
					/deep/ span.swiper-pagination-bullet:hover {
						background: #fff;
						opacity: 1;
						transform: scale(1.2);
					}
					/deep/ span.swiper-pagination-bullet.swiper-pagination-bullet-active {
						background: #fff;
						opacity: 1;
						width: 30px;
						border-radius: 5px;
					}
				}
				.swiper-button-next {
					margin: -20px 60px 0 0;
					top: 50%;
					width: 50px;
					height: 50px;
					background: rgba(255,255,255,0.3);
					border-radius: 50%;
					transition: all 0.3s ease;
					.icon {
						color: #fff;
						width: 24px;
						font-size: 24px;
						height: 24px;
					}
				}
				.swiper-button-next:hover {
					background: rgba(255,255,255,0.5);
				}
				.swiper-button-prev {
					margin: -20px 0 0 60px;
					top: 50%;
					width: 50px;
					height: 50px;
					background: rgba(255,255,255,0.3);
					border-radius: 50%;
					transition: all 0.3s ease;
					.icon {
						color: #fff;
						width: 24px;
						font-size: 24px;
						height: 24px;
					}
				}
				.swiper-button-prev:hover {
					background: rgba(255,255,255,0.5);
				}
			}
			.banner-toggle-btn {
				margin: 10px auto;
				padding: 8px 20px;
				background: linear-gradient(135deg, #5DB5B7 0%, #3a9a9c 100%);
				color: #fff;
				border-radius: 20px;
				display: flex;
				align-items: center;
				justify-content: center;
				gap: 5px;
				cursor: pointer;
				width: fit-content;
				font-size: 14px;
				transition: all 0.3s ease;
				box-shadow: 0 2px 10px rgba(93,181,183,0.3);
				.icon {
					font-size: 12px;
					transform: rotate(180deg);
				}
			}
			.banner-toggle-btn:hover {
				transform: translateY(-2px);
				box-shadow: 0 4px 15px rgba(93,181,183,0.4);
			}
			#scrollView.has-top-margin .breadcrumb-preview {
				padding-top: 85px;
			}
			.bottom-preview {
				width: 100%;
				height: auto;
				.footer {
					padding: 30px calc(50% - 625px);
					margin: 0 auto;
					overflow: hidden;
					color: #fff;
					background: linear-gradient(135deg, #2c3e50 0%, #1a252f 100%);
					width: 100%;
					min-height: 100px;
					text-align: center;
					height: auto;
					font-size: 14px;
					line-height: 1.8;
				}
			}
		}
	}
	.chat-content {
		padding-bottom: 20px;
		width: 100%;
		margin-bottom: 10px;
		max-height: 300px;
		height: 300px;
		overflow-y: scroll;
		border: 1px solid #eeeeee;
		background: #fff;
		border-radius: 8px;

		.left-content {
			float: left;
			margin-bottom: 10px;
			padding: 10px;
			max-width: 80%;
		}

		.right-content {
			float: right;
			margin-bottom: 10px;
			padding: 10px;
			max-width: 80%;
		}
	}

	.clear-float {
		clear: both;
	}
	.emoji-mart[data-v-7bc71df8] {
		font-family: -apple-system, BlinkMacSystemFont, "Helvetica Neue", sans-serif;
		display: -ms-flexbox;
		display: flex;
		-ms-flex-direction: column;
		flex-direction: column;
		height: 420px;
		color: #ffffff !important;
		border: 1px solid #d9d9d9;
		border-radius: 5px;
		background: #fff;
	}
</style>
