<template>
	<div>
		<div class="login-container">
			<el-form ref="loginForm" :model="loginForm" :rules="rules" class="login_form animate__animated animate__fadeInDown">
				<div class="login_form2">
					<div class="login-title">好物集市</div>
					<div v-if="loginType==1" class="list-item" prop="username">
						<div class="lable">
							账号：
						</div>
						<input v-model="loginForm.username" placeholder="请输入账号：">
					</div>
					<div v-if="loginType==1" class="list-item" prop="password">
						<div class="lable">
							密码：
						</div>
						<div class="password-box">
							<input v-model="loginForm.password" placeholder="请输入密码：" :type="showPassword?'text':'password'">
							<span class="icon iconfont" :class="showPassword?'icon-liulan13':'icon-liulan17'" @click="showPassword=!showPassword"></span>
						</div>
					</div>

					<div class="list-item select" v-if="roles.length>1">
						<div class="lable">
							角色：
						</div>
						<el-select v-model="loginForm.tableName" placeholder="请选择角色：" @change="selectChange">
							<el-option v-for="item,index in roles" :key="index" :label="item.roleName" :value="item.tableName" />
						</el-select>
					</div>

			
					<div class="list-btn">
						<el-button class="login_btn" v-if="loginType==1" @click="submitForm('loginForm')">登录</el-button>

						<div class="list-btn2">
							<router-link class="register_btn" :to="{path: '/register', query: {role: item.tableName,pageFlag:'register'}}" v-if="item.hasFrontRegister=='是'" v-for="(item, index) in roles" :key="index">注册{{item.roleName.replace('注册','')}}</router-link>
						</div>
					</div>
				</div>
				<div class="idea1"></div>
				<div class="idea2"></div>
			</el-form>
		</div>
	</div>
</template>

<script>
	import 'animate.css';
import menu from '@/config/menu'
export default {
	//数据集合
	data() {
		return {
            baseUrl: this.$config.baseUrl,
            loginType: 1,
			roleMenus: [],
			loginForm: {
				username: '',
				password: '',
				tableName: '',
				code: '',
			},
			role: '',
            roles: [],
			rules: {
				username: [
					{ required: true, message: '请输入账号', trigger: 'blur' }
				],
				password: [
					{ required: true, message: '请输入密码', trigger: 'blur' }
				]
			},
			codes: [{
				num: 1,
				color: '#000',
				rotate: '10deg',
				size: '16px'
			}, {
				num: 2,
				color: '#000',
				rotate: '10deg',
				size: '16px'
			}, {
				num: 3,
				color: '#000',
				rotate: '10deg',
				size: '16px'
			}, {
				num: 4,
				color: '#000',
				rotate: '10deg',
				size: '16px'
			}],
			flag: false,
			verifyCheck2: false,
			showPassword: false,
		}
	},
	components: {
	},
	created() {
		this.roleMenus = menu.list()
		for(let item in this.roleMenus) {
			if(this.roleMenus[item].hasFrontLogin=='是') {
				this.roles.push(this.roleMenus[item]);
			}
		}
		
	},
	mounted() {
	},
	//方法集合
	methods: {
		randomString() {
			var len = 4;
			var chars = [
				'a', 'b', 'c', 'd', 'e', 'f', 'g', 'h', 'i', 'j', 'k',
				'l', 'm', 'n', 'o', 'p', 'q', 'r', 's', 't', 'u', 'v',
				'w', 'x', 'y', 'z', 'A', 'B', 'C', 'D', 'E', 'F', 'G',
				'H', 'I', 'J', 'K', 'L', 'M', 'N', 'O', 'P', 'Q', 'R',
				'S', 'T', 'U', 'V', 'W', 'X', 'Y', 'Z', '0', '1', '2',
				'3', '4', '5', '6', '7', '8', '9'
			]
			var colors = ['0', '1', '2', '3', '4', '5', '6', '7', '8', '9', 'a', 'b', 'c', 'd', 'e', 'f']
			var sizes = ['14', '15', '16', '17', '18']
			
			var output = []
			for (var i = 0; i < len; i++) {
				// 随机验证码
				var key = Math.floor(Math.random() * chars.length)
				this.codes[i].num = chars[key]
				// 随机验证码颜色
				var code = '#'
				for (var j = 0; j < 6; j++) {
					var key = Math.floor(Math.random() * colors.length)
					code += colors[key]
				}
				this.codes[i].color = code
				// 随机验证码方向
				var rotate = Math.floor(Math.random() * 45)
				var plus = Math.floor(Math.random() * 2)
				if (plus == 1) rotate = '-' + rotate
				this.codes[i].rotate = 'rotate(' + rotate + 'deg)'
				// 随机验证码字体大小
				var size = Math.floor(Math.random() * sizes.length)
				this.codes[i].size = sizes[size] + 'px'
			}
		},
		selectChange(e){
			for(let x in this.roles){
				if(this.roles[x].tableName == e){
					this.role = this.roles[x].roleName
				}
			}
		},
		submitForm(formName) {
			if (this.roles.length!=1) {
				if (!this.role) {
					this.$message.error("请选择登录用户类型");
					return false;
				}
			} else {
				this.role = this.roles[0].roleName;
				this.loginForm.tableName = this.roles[0].tableName;
			}
			if (!this.loginForm.username) {
				this.$message.error("请输入用户名");
				return;
			}
			if (!this.loginForm.password) {
				this.$message.error("请输入密码");
				return;
			}

			this.loginPost(formName)
		},
		loginPost(formName) {
			this.$refs[formName].validate((valid) => {
				if (valid) {
					this.$http.get(`${this.loginForm.tableName}/login`, {params: this.loginForm}).then(res => {
						if (res.data.code === 0) {
							localStorage.setItem('frontToken', res.data.token);
							localStorage.setItem('UserTableName', this.loginForm.tableName);
							localStorage.setItem('username', this.loginForm.username);
							// localStorage.setItem('adminName', this.loginForm.username);
							localStorage.setItem('frontSessionTable', this.loginForm.tableName);
							localStorage.setItem('frontRole', this.role);
							localStorage.setItem('keyPath', 0);
							this.$router.push('/');
							this.$message({
								message: '登录成功',
								type: 'success',
								duration: 1500,
							});
						} else {
							this.$message.error(res.data.msg);
						}
					});
				} else {
					return false;
				}
			});
		},
    }
}
</script>

<style rel="stylesheet/scss" lang="scss" scoped>
	.login-container {
		background: #f5f5f5;
		display: flex;
		width: 100%;
		min-height: 100vh;
		justify-content: center;
		align-items: center;
		background-position: center center;
		position: relative;
		.login_form {
			padding: 0;
			margin: 0;
			background: rgba(255, 255, 255, 0.95);
			width: 500px;
			height: auto;
			border-radius: 20px;
			box-shadow: 0 20px 60px rgba(0, 0, 0, 0.3);
			backdrop-filter: blur(10px);
			.login_form2 {
				padding: 40px 50px;
				margin: 0;
				background: transparent;
				width: 100%;
				.login-title {
					margin: 0 0 40px 0;
					color: #5DB5B7;
					font-weight: 700;
					width: 100%;
					font-size: 32px;
					line-height: 1;
					text-align: center;
					letter-spacing: 2px;
				}
				.list-item {
					border: 2px solid #e0e0e0;
					border-radius: 12px;
					box-shadow: none;
					margin: 0 0 25px 0;
					background: #fff;
					display: flex;
					width: 100%;
					align-items: center;
					transition: all 0.3s ease;
					.lable {
						color: #666;
						width: 80px;
						font-size: 15px;
						line-height: 50px;
						text-align: center;
						font-weight: 500;
					}
					input {
						border: 0px solid #ddd;
						padding: 0 15px;
						color: #333;
						flex: 1;
						background: transparent;
						width: 100%;
						font-size: 15px;
						height: 50px;
					}
					input:focus {
						border: 0px solid #f7db61;
						padding: 0 15px;
						outline: none;
						color: #333;
						flex: 1;
						background: transparent;
						width: 100%;
						font-size: 15px;
						height: 50px;
					}
					.password-box {
						flex: 1;
						display: flex;
						width: 100%;
						position: relative;
						align-items: center;
						input {
							border: 0px solid #ddd;
							padding: 0 15px;
							color: #333;
							flex: 1;
							background: transparent;
							width: 100%;
							font-size: 15px;
							height: 50px;
						}
						input:focus {
							border: 0px solid #f7db61;
							padding: 0 15px;
							outline: none;
							color: #333;
							flex: 1;
							background: transparent;
							width: 100%;
							font-size: 15px;
							height: 50px;
						}
						.iconfont {
							cursor: pointer;
							z-index: 1;
							color: #999;
							top: 0;
							font-size: 18px;
							line-height: 50px;
							position: absolute;
							right: 15px;
							transition: color 0.3s ease;
						}
						.iconfont:hover {
							color: #5DB5B7;
						}
					}
					input::placeholder {
						color: #bbb;
						font-size: 14px;
					}
				}
				.list-item:hover {
					border-color: #5DB5B7;
					box-shadow: 0 4px 12px rgba(93, 181, 183, 0.2);
				}
				.select {
					border: 2px solid #e0e0e0;
					border-radius: 12px;
					box-shadow: none;
					margin: 0 0 25px 0;
					background: #fff;
					display: flex;
					width: 100%;
					align-items: center;
					transition: all 0.3s ease;
					/deep/ .el-select {
						width: 100%;
					}
					/deep/ .el-select .el-input__inner {
						border: none;
						padding: 0 15px;
						box-shadow: none;
						color: #333;
						background: transparent;
						width: 100%;
						font-size: 15px;
						height: 50px;
					}
					/deep/ .el-select .is-focus .el-input__inner {
						border: none;
						padding: 0 15px;
						box-shadow: none;
						outline: none;
						color: #333;
						background: transparent;
						width: 100%;
						font-size: 15px;
						height: 50px;
					}
					/deep/ .el-select .el-input__inner::placeholder{
						color: #bbb;
						font-size: 14px;
					}
				}
				.select:hover {
					border-color: #5DB5B7;
					box-shadow: 0 4px 12px rgba(93, 181, 183, 0.2);
				}
				.list-btn {
					padding: 0;
					margin: 0 auto 20px;
					width: 100%;
					.login_btn {
						border: 0;
						cursor: pointer;
						border-radius: 12px;
						padding: 0 30px;
						margin: 0 0px;
						color: #fff;
						background: linear-gradient(135deg, #5DB5B7 0%, #3a9a9c 100%);
						width: 100%;
						font-size: 18px;
						height: 55px;
						font-weight: 600;
						letter-spacing: 1px;
						transition: all 0.3s ease;
						box-shadow: 0 8px 20px rgba(93, 181, 183, 0.3);
					}
					.login_btn:hover {
						background: linear-gradient(135deg, #4aa5a7 0%, #2d8587 100%);
						transform: translateY(-2px);
						box-shadow: 0 12px 28px rgba(93, 181, 183, 0.4);
					}
					.list-btn2 {
						margin: 20px auto 0;
						display: flex;
						width: 100%;
						flex-wrap: wrap;
						justify-content: center;
						.register_btn {
							cursor: pointer;
							padding: 8px 20px;
							margin: 0 10px 10px 0;
							color: #5DB5B7;
							background: transparent;
							text-decoration: none;
							font-size: 15px;
							border: 1px solid #5DB5B7;
							border-radius: 20px;
							transition: all 0.3s ease;
						}
						.register_btn:hover {
							color: #fff;
							background: #5DB5B7;
						}
						.resetpwd_btn {
							cursor: pointer;
							padding: 8px 20px;
							margin: 0 10px 10px 0;
							color: #5DB5B7;
							background: transparent;
							text-decoration: none;
							font-size: 15px;
							border: 1px solid #5DB5B7;
							border-radius: 20px;
							transition: all 0.3s ease;
						}
						.resetpwd_btn:hover {
							color: #fff;
							background: #5DB5B7;
						}
					}
				}
			}
			.idea1 {
				background: red;
				display: none;
				width: 100%;
				height: 40px;
			}
			.idea2 {
				background: blue;
				display: none;
				width: 100%;
				height: 40px;
			}
		}
	}
</style>
