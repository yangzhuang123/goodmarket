<template>
	<div class="register-container">
		<div class="register-left">
			<div class="register-left-content">
				<h1 class="brand-title">好物集市</h1>
				<p class="brand-subtitle">一站式购物管理平台</p>
				<div class="features">
					<div class="feature-item">
						<i class="el-icon-goods"></i>
						<span>商品管理</span>
					</div>
					<div class="feature-item">
						<i class="el-icon-document"></i>
						<span>订单处理</span>
					</div>
					<div class="feature-item">
						<i class="el-icon-user"></i>
						<span>用户管理</span>
					</div>
				</div>
			</div>
		</div>
		<div class="register-right">
			<div class="register-form-wrapper animate__animated animate__fadeInRight">
				<div class="form-header">
					<h2>欢迎注册</h2>
					<p v-if="tableName=='shangjia'">创建商家账号，开始您的创业之旅</p>
					<p v-else>创建用户账号，享受购物乐趣</p>
				</div>
				<el-form ref="ruleForm" class="register-form" :model="ruleForm" :rules="rules">
					<div class="form-group" v-if="tableName=='yonghu'">
						<label :class="changeRules('yonghuming')?'required':''">用户名</label>
						<div class="input-wrapper">
							<i class="el-icon-user"></i>
							<el-input v-model="ruleForm.yonghuming" autocomplete="off" placeholder="请输入用户名" type="text" />
						</div>
					</div>
					<div class="form-group">
						<label :class="changeRules('mima')?'required':''">密码</label>
						<div class="input-wrapper">
							<i class="el-icon-lock"></i>
							<el-input v-model="ruleForm.mima" autocomplete="off" placeholder="请输入密码" type="password" />
						</div>
					</div>
					<div class="form-group">
						<label :class="changeRules('mima')?'required':''">确认密码</label>
						<div class="input-wrapper">
							<i class="el-icon-lock"></i>
							<el-input v-model="ruleForm.mima2" autocomplete="off" placeholder="请确认密码" type="password" />
						</div>
					</div>
					<div class="form-group" v-if="tableName=='yonghu'">
						<label :class="changeRules('xingming')?'required':''">姓名</label>
						<div class="input-wrapper">
							<i class="el-icon-user"></i>
							<el-input v-model="ruleForm.xingming" autocomplete="off" placeholder="请输入姓名" type="text" />
						</div>
					</div>
					<div class="form-group" v-if="tableName=='shangjia'">
						<label :class="changeRules('dianpumingcheng')?'required':''">店铺名称</label>
						<div class="input-wrapper">
							<i class="el-icon-shop"></i>
							<el-input v-model="ruleForm.dianpumingcheng" autocomplete="off" placeholder="请输入店铺名称" type="text" />
						</div>
					</div>
					<div class="form-group" v-if="tableName=='shangjia'">
						<label :class="changeRules('shangjiaxingming')?'required':''">商家姓名</label>
						<div class="input-wrapper">
							<i class="el-icon-user"></i>
							<el-input v-model="ruleForm.shangjiaxingming" autocomplete="off" placeholder="请输入商家姓名" type="text" />
						</div>
					</div>
					<div class="form-group">
						<label>头像</label>
						<file-upload
							tip="点击上传头像"
							action="file/upload"
							:limit="3"
							:multiple="true"
							:fileUrls="ruleForm.touxiang?ruleForm.touxiang:''"
							@change="tableName=='yonghu' ? yonghutouxiangUploadChange : shangjiatouxiangUploadChange"
						></file-upload>
					</div>
					<div class="form-group">
						<label>性别</label>
						<div class="input-wrapper">
							<i class="el-icon-user"></i>
							<el-select v-model="ruleForm.xingbie" placeholder="请选择性别" >
								<el-option
									v-for="(item,index) in tableName=='yonghu' ? yonghuxingbieOptions : shangjiaxingbieOptions"
									v-bind:key="index"
									:label="item"
									:value="item">
								</el-option>
							</el-select>
						</div>
					</div>
					<div class="form-group">
						<label>年龄</label>
						<div class="input-wrapper">
							<i class="el-icon-time"></i>
							<el-input v-model.number="ruleForm.nianling" autocomplete="off" placeholder="请输入年龄" type="text" />
						</div>
					</div>
					<div class="form-group">
						<label>邮箱</label>
						<div class="input-wrapper">
							<i class="el-icon-message"></i>
							<el-input v-model="ruleForm.youxiang" autocomplete="off" placeholder="请输入邮箱" type="text" />
						</div>
					</div>
					<div class="form-group">
						<label :class="changeRules('mobile')?'required':''">手机号码</label>
						<div class="input-wrapper">
							<i class="el-icon-mobile"></i>
							<input v-model="ruleForm.mobile" autocomplete="off" placeholder="请输入手机号"/>
							<button v-if="isEndFlag" type="success" class="register-code" @click="sendsmscode()">发送验证码</button>
							<button v-if="!isEndFlag" type="success" class="register-code" disabled="disabled">{{SecondToDate}}</button>
						</div>
					</div>
					<div class="form-group">
						<label :class="changeRules('mobile')?'required':''">验证码</label>
						<div class="input-wrapper">
							<i class="el-icon-key"></i>
							<el-input v-model="smscode" autocomplete="off" placeholder="请输入验证码" />
						</div>
					</div>
					<div class="form-actions">
						<el-button 
							type="primary" 
							@click="login()" 
							class="register-btn"
						>
							注册
						</el-button>
					</div>
					<div class="form-footer">
						<span>已有账号？</span>
						<el-button type="text" @click="close()">直接登录</el-button>
					</div>
				</el-form>
			</div>
		</div>
	</div>
</template>

<script>
	import 'animate.css'
export default {
	data() {
		return {
			ruleForm: {
			},
			forgetForm: {},
            pageFlag : '',
			tableName:"",
			rules: {},
            smscode:'',
			// 倒计时时间
			count: 60,
			// 倒计时定时器
			inter: null,
			// 倒计时是否结束
			isEndFlag: true,
            yonghuxingbieOptions: [],
            shangjiaxingbieOptions: [],
		};
	},
	computed: {
		SecondToDate: function() {
			var time = this.count;
			if (null != time && "" != time) {
				time = parseInt(time) + "秒后重发";
			}
			return time;
		}
	},
	mounted(){
		this.pageFlag = this.$route.query.pageFlag
		if(this.$route.query.pageFlag=='register'){
			
			let table = this.$storage.get("loginTable");
			this.tableName = table;
			if(this.tableName=='yonghu'){
				this.ruleForm = {
					yonghuming: '',
					mima: '',
					xingming: '',
					touxiang: '',
					xingbie: '',
					nianling: '',
					youxiang: '',
					mobile: '',
					money: '',
				}
			}
			if(this.tableName=='shangjia'){
				this.ruleForm = {
					dianpumingcheng: '',
					mima: '',
					shangjiaxingming: '',
					touxiang: '',
					xingbie: '',
					nianling: '',
					youxiang: '',
					mobile: '',
					money: '',
				}
			}
			if ('yonghu' == this.tableName) {
				this.rules.yonghuming = [{ required: true, message: '请输入用户名', trigger: 'blur' }]
			}
			if ('yonghu' == this.tableName) {
				this.rules.mima = [{ required: true, message: '请输入密码', trigger: 'blur' }]
			}
			if ('yonghu' == this.tableName) {
				this.rules.xingming = [{ required: true, message: '请输入姓名', trigger: 'blur' }]
			}
			if ('shangjia' == this.tableName) {
				this.rules.dianpumingcheng = [{ required: true, message: '请输入店铺名称', trigger: 'blur' }]
			}
			if ('shangjia' == this.tableName) {
				this.rules.mima = [{ required: true, message: '请输入密码', trigger: 'blur' }]
			}
			if ('shangjia' == this.tableName) {
				this.rules.shangjiaxingming = [{ required: true, message: '请输入商家姓名', trigger: 'blur' }]
			}
			this.yonghuxingbieOptions = "男,女".split(',')
			this.shangjiaxingbieOptions = "男,女".split(',')
		}
	},
	created() {
	},
	destroyed() {
		  	},
	methods: {
		changeRules(name){
			if(this.rules[name]){
				return true
			}
			return false
		},
		// 获取uuid
		getUUID () {
			return new Date().getTime();
		},
		close(){
			this.$router.push({ path: "/login" });
		},
        yonghutouxiangUploadChange(fileUrls) {
            this.ruleForm.touxiang = fileUrls;
        },
        shangjiatouxiangUploadChange(fileUrls) {
            this.ruleForm.touxiang = fileUrls;
        },

        // 多级联动参数

		sendsmscode() {
			if(!this.ruleForm.mobile){
				this.$message.error(`手机号码不能为空`);
				return
			}
			if(this.ruleForm.mobile&&(!this.$validate.isMobile(this.ruleForm.mobile))){
				this.$message.error(`请输入正确的手机格式`);
				return
			}
			this.isEndFlag = false;
			var _this = this;
			this.inter = window.setInterval(function() {
				_this.count = _this.count - 1;
				if (_this.count <= 0) {
					window.clearInterval(_this.inter);
					_this.isEndFlag = true;
					_this.count=60;
				}
			}, 1000);
			this.$http({
				url: `${this.tableName}/sendsms?mobile=`+this.ruleForm.mobile,
				method: "get",
				params: {}
			}).then(({ data }) => {
				if (data && data.code === 0) {
					this.$message.success(`发送成功`);
				} else {
					this.$message.error(`服务器异常，请稍后重试`)
				}
			});
		},

		// 注册
		login() {
			var url=this.tableName+"/register";
			if((!this.ruleForm.yonghuming) && `yonghu` == this.tableName){
				this.$message.error(`用户名不能为空`);
				return
			}
			if((!this.ruleForm.mima) && `yonghu` == this.tableName){
				this.$message.error(`密码不能为空`);
				return
			}
			if((this.ruleForm.mima!=this.ruleForm.mima2) && `yonghu` == this.tableName){
				this.$message.error(`两次密码输入不一致`);
				return
			}
			if((!this.ruleForm.xingming) && `yonghu` == this.tableName){
				this.$message.error(`姓名不能为空`);
				return
			}
            if(this.ruleForm.touxiang!=null) {
                this.ruleForm.touxiang = this.ruleForm.touxiang.replace(new RegExp(this.$base.url,"g"),"");
            }
			if(`yonghu` == this.tableName && this.ruleForm.nianling &&(!this.$validate.isIntNumer(this.ruleForm.nianling))){
				this.$message.error(`年龄应输入整数`);
				return
			}
			if(`yonghu` == this.tableName && this.ruleForm.youxiang &&(!this.$validate.isEmail(this.ruleForm.youxiang))){
				this.$message.error(`邮箱应输入邮件格式`);
				return
			}
			if(`yonghu` == this.tableName && this.ruleForm.mobile &&(!this.$validate.isMobile(this.ruleForm.mobile))){
				this.$message.error(`手机号应输入手机格式`);
				return
			}
			if(`yonghu` == this.tableName ){
				url=this.tableName+"/register?smscode="+this.smscode;
				if(this.ruleForm.mobile&&(!this.$validate.isMobile(this.ruleForm.mobile))){
					this.$message.error(`请输入正确的手机格式`);
					return
				}
			}
			if((!this.smscode) && `yonghu` == this.tableName){
				this.$message.error(`验证码不能为空`);
				return
			}
			if((!this.ruleForm.dianpumingcheng) && `shangjia` == this.tableName){
				this.$message.error(`店铺名称不能为空`);
				return
			}
			if((!this.ruleForm.mima) && `shangjia` == this.tableName){
				this.$message.error(`密码不能为空`);
				return
			}
			if((this.ruleForm.mima!=this.ruleForm.mima2) && `shangjia` == this.tableName){
				this.$message.error(`两次密码输入不一致`);
				return
			}
			if((!this.ruleForm.shangjiaxingming) && `shangjia` == this.tableName){
				this.$message.error(`商家姓名不能为空`);
				return
			}
            if(this.ruleForm.touxiang!=null) {
                this.ruleForm.touxiang = this.ruleForm.touxiang.replace(new RegExp(this.$base.url,"g"),"");
            }
			if(`shangjia` == this.tableName && this.ruleForm.nianling &&(!this.$validate.isIntNumer(this.ruleForm.nianling))){
				this.$message.error(`年龄应输入整数`);
				return
			}
			if(`shangjia` == this.tableName && this.ruleForm.youxiang &&(!this.$validate.isEmail(this.ruleForm.youxiang))){
				this.$message.error(`邮箱应输入邮件格式`);
				return
			}
			if(`shangjia` == this.tableName && this.ruleForm.mobile &&(!this.$validate.isMobile(this.ruleForm.mobile))){
				this.$message.error(`手机号应输入手机格式`);
				return
			}
			if(`shangjia` == this.tableName ){
				url=this.tableName+"/register?smscode="+this.smscode;
				if(this.ruleForm.mobile&&(!this.$validate.isMobile(this.ruleForm.mobile))){
					this.$message.error(`请输入正确的手机格式`);
					return
				}
			}
			if((!this.smscode) && `shangjia` == this.tableName){
				this.$message.error(`验证码不能为空`);
				return
			}
			this.$http({
				url: url,
				method: "post",
				data:this.ruleForm
			}).then(({ data }) => {
				if (data && data.code === 0) {
					this.$message({
						message: "注册成功",
						type: "success",
						duration: 1500,
						onClose: () => {
							this.$router.replace({ path: "/login" });
						}
					});
				} else {
					this.$message.error(data.msg);
				}
			});
		}
	}
};
</script>

<style lang="scss" scoped>
.register-container {
	display: flex;
	min-height: 100vh;
	width: 100%;
}

.register-left {
	flex: 1;
	background: linear-gradient(135deg, #667eea 0%, #764ba2 100%);
	display: flex;
	align-items: center;
	justify-content: center;
	position: relative;
	overflow: hidden;

	&::before {
		content: '';
		position: absolute;
		top: -50%;
		left: -50%;
		width: 200%;
		height: 200%;
		background: radial-gradient(circle, rgba(255,255,255,0.1) 0%, transparent 60%);
		animation: pulse 15s ease-in-out infinite;
	}

	@keyframes pulse {
		0%, 100% { transform: scale(1); }
		50% { transform: scale(1.1); }
	}

	.register-left-content {
		text-align: center;
		color: #fff;
		z-index: 1;
		padding: 40px;

		.brand-title {
			font-size: 48px;
			font-weight: 700;
			margin-bottom: 16px;
			text-shadow: 2px 2px 4px rgba(0,0,0,0.2);
		}

		.brand-subtitle {
			font-size: 18px;
			opacity: 0.9;
			margin-bottom: 60px;
		}

		.features {
			display: flex;
			gap: 40px;
			justify-content: center;

			.feature-item {
				display: flex;
				flex-direction: column;
				align-items: center;
				gap: 12px;

				i {
					font-size: 32px;
					background: rgba(255,255,255,0.2);
					width: 64px;
					height: 64px;
					border-radius: 50%;
					display: flex;
					align-items: center;
					justify-content: center;
				}

				span {
					font-size: 14px;
					opacity: 0.9;
				}
			}
		}
	}
}

.register-right {
	flex: 1;
	display: flex;
	align-items: center;
	justify-content: center;
	background: #f8fafc;
	padding: 40px;

	.register-form-wrapper {
		width: 100%;
		max-width: 480px;
		background: #fff;
		border-radius: 16px;
		padding: 48px 40px;
		box-shadow: 0 20px 60px rgba(0,0,0,0.1);
		overflow-y: auto;
		max-height: 90vh;

		.form-header {
			text-align: center;
			margin-bottom: 32px;

			h2 {
				font-size: 28px;
				font-weight: 600;
				color: #1a1a2e;
				margin-bottom: 8px;
			}

			p {
				color: #6b7280;
				font-size: 14px;
			}
		}

		.register-form {
			.form-group {
				margin-bottom: 24px;

				label {
					display: block;
					font-size: 14px;
					font-weight: 500;
					color: #374151;
					margin-bottom: 8px;

					&.required {
						position: relative;

						&::after {
							content: '*';
							color: #ef4444;
							margin-left: 4px;
						}
					}
				}

				.input-wrapper {
					position: relative;
					display: flex;
					align-items: center;

					> i:first-child {
						position: absolute;
						left: 16px;
						color: #9ca3af;
						font-size: 18px;
					}

					.el-input {
						width: 100%;

						::v-deep .el-input__inner {
							width: 100%;
							height: 48px;
							padding: 0 48px;
							border: 2px solid #e5e7eb;
							border-radius: 10px;
							font-size: 15px;
							color: #1f2937;
							transition: all 0.3s ease;
							background: #f9fafb;

							&:focus {
								outline: none;
								border-color: #667eea;
								background: #fff;
								box-shadow: 0 0 0 4px rgba(102, 126, 234, 0.1);
							}

							&::placeholder {
								color: #9ca3af;
							}
						}
					}

					.el-select {
						width: 100%;

						::v-deep .el-input__inner {
							width: 100%;
							height: 48px;
							padding: 0 48px;
							border: 2px solid #e5e7eb;
							border-radius: 10px;
							font-size: 15px;
							color: #1f2937;
							transition: all 0.3s ease;
							background: #f9fafb;

							&:focus {
								outline: none;
								border-color: #667eea;
								background: #fff;
								box-shadow: 0 0 0 4px rgba(102, 126, 234, 0.1);
							}
						}
					}

					input {
						width: 100%;
						height: 48px;
						padding: 0 16px;
						border: 2px solid #e5e7eb;
						border-radius: 10px 0 0 10px;
						font-size: 15px;
						color: #1f2937;
						transition: all 0.3s ease;
						background: #f9fafb;

						&:focus {
							outline: none;
							border-color: #667eea;
							background: #fff;
							box-shadow: 0 0 0 4px rgba(102, 126, 234, 0.1);
						}

						&::placeholder {
							color: #9ca3af;
						}
					}

					.register-code {
						border: none;
						cursor: pointer;
						border-radius: 0 10px 10px 0;
						padding: 0 20px;
						color: #667eea;
						background: rgba(102, 126, 234, 0.1);
						font-size: 14px;
						height: 48px;
						font-weight: 500;
						transition: all 0.3s ease;

						&:hover:not(:disabled) {
							background: rgba(102, 126, 234, 0.2);
						}

						&:disabled {
							color: #9ca3af;
							cursor: not-allowed;
						}
					}
				}

				::v-deep .el-upload--picture-card {
					background: transparent;
					border: 0;
					border-radius: 0;
					width: auto;
					height: auto;
					line-height: initial;
					vertical-align: middle;
				}

				::v-deep .upload .upload-img {
					border: 2px dashed #e5e7eb;
					cursor: pointer;
					border-radius: 10px;
					color: #9ca3af;
					background: #f9fafb;
					width: 100px;
					font-size: 24px;
					line-height: 80px;
					text-align: center;
					height: 80px;
					transition: all 0.3s ease;

					&:hover {
						border-color: #667eea;
						background: rgba(102, 126, 234, 0.05);
					}
				}

				::v-deep .el-upload-list .el-upload-list__item {
					border: 2px solid #e5e7eb;
					cursor: pointer;
					border-radius: 10px;
					color: #999;
					background: #fff;
					width: 100px;
					font-size: 24px;
					line-height: 80px;
					text-align: center;
					height: 80px;
				}

				::v-deep .el-upload .el-icon-plus {
					border: 2px dashed #e5e7eb;
					cursor: pointer;
					border-radius: 10px;
					color: #9ca3af;
					background: #f9fafb;
					width: 100px;
					font-size: 24px;
					line-height: 80px;
					text-align: center;
					height: 80px;
					transition: all 0.3s ease;

					&:hover {
						border-color: #667eea;
						background: rgba(102, 126, 234, 0.05);
					}
				}

				::v-deep .el-upload__tip {
					color: #6b7280;
					font-size: 14px;
					margin-top: 8px;
				}
			}

			.form-actions {
				margin-top: 32px;

				.register-btn {
					width: 100%;
					height: 48px;
					font-size: 16px;
					font-weight: 600;
					border-radius: 10px;
					background: linear-gradient(135deg, #667eea 0%, #764ba2 100%);
					border: none;
					transition: all 0.3s ease;

					&:hover {
						transform: translateY(-2px);
						box-shadow: 0 8px 25px rgba(102, 126, 234, 0.4);
					}

					&:active {
						transform: translateY(0);
					}
				}
			}

			.form-footer {
				text-align: center;
				margin-top: 24px;
				padding-top: 24px;
				border-top: 1px solid #e5e7eb;
				color: #6b7280;
				font-size: 14px;

				.el-button--text {
					color: #667eea;
					font-weight: 500;
					padding: 0 4px;

					&:hover {
						color: #764ba2;
					}
				}
			}
		}
	}
}

@media (max-width: 768px) {
	.register-container {
		flex-direction: column;
	}

	.register-left {
		padding: 60px 20px;

		.register-left-content {
			.brand-title {
				font-size: 36px;
			}

			.brand-subtitle {
				margin-bottom: 30px;
			}

			.features {
				gap: 20px;

				.feature-item {
					i {
						font-size: 24px;
						width: 50px;
						height: 50px;
					}

					span {
						font-size: 12px;
					}
				}
			}
		}
	}

	.register-right {
		padding: 30px 20px;

		.register-form-wrapper {
			padding: 32px 24px;
			max-height: 80vh;
		}
	}
}
</style>
