<template>
	<div>

		<div class="container">
			<el-form class='rgs-form animate__animated animate__fadeInDown' v-if="pageFlag=='register'" ref="registerForm" :model="registerForm" :rules="rules">
				<div class="rgs-form2">
					<div class="title">好物集市</div>
					<el-form-item class="list-item" v-if="tableName=='yonghu'" prop="yonghuming">
						<div class="label" :class="changeRules('yonghuming')?'required':''">用户名：</div>
						<el-input v-model="registerForm.yonghuming"  placeholder="请输入用户名" />
					</el-form-item>
					<el-form-item class="list-item" v-if="tableName=='yonghu'" prop="mima">
						<div class="label" :class="changeRules('mima')?'required':''">密码：</div>
						<el-input v-model="registerForm.mima" type="password" placeholder="请输入密码" />
					</el-form-item>
					<el-form-item class="list-item" v-if="tableName=='yonghu'" prop="mima2">
						<div class="label" :class="changeRules('mima')?'required':''">确认密码：</div>
						<el-input v-model="registerForm.mima2" type="password" placeholder="请再次输入密码" />
					</el-form-item>
					<el-form-item class="list-item" v-if="tableName=='yonghu'" prop="xingming">
						<div class="label" :class="changeRules('xingming')?'required':''">姓名：</div>
						<el-input v-model="registerForm.xingming"  placeholder="请输入姓名" />
					</el-form-item>
					<el-form-item class="list-item" v-if="tableName=='yonghu'" prop="touxiang">
						<div class="label" :class="changeRules('touxiang')?'required':''">头像：</div>
						<file-upload
							tip="点击上传头像"
							action="file/upload"
							:limit="1"
							:multiple="true"
							:fileUrls="registerForm.touxiang?registerForm.touxiang:''"
							@change="yonghutouxiangUploadChange"
						></file-upload>
					</el-form-item>
					<el-form-item class="list-item" v-if="tableName=='yonghu'" prop="xingbie">
						<div class="label" :class="changeRules('xingbie')?'required':''">性别：</div>
						<el-select v-model="registerForm.xingbie" placeholder="请选择性别" >
							<el-option
								v-for="(item,index) in yonghuxingbieOptions"
								:key="index"
								:label="item"
								:value="item">
							</el-option>
						</el-select>
					</el-form-item>
					<el-form-item class="list-item" v-if="tableName=='yonghu'" prop="nianling">
						<div class="label" :class="changeRules('nianling')?'required':''">年龄：</div>
						<el-input v-model.number="registerForm.nianling"  placeholder="请输入年龄" />
					</el-form-item>
					<el-form-item class="list-item" v-if="tableName=='yonghu'" prop="youxiang">
						<div class="label" :class="changeRules('youxiang')?'required':''">邮箱：</div>
						<el-input v-model="registerForm.youxiang"  placeholder="请输入邮箱" />
					</el-form-item>
					<el-form-item v-if="tableName=='yonghu'" class="el-email list-item">
						<div class="label required">手机号：</div>
						<div style="display: flex;flex: 1;">
							<input v-model="registerForm.mobile" type="text" id="mobile-yonghu" name="mobile" placeholder="请输入手机号" autocomplete="off" class="emailInput">
							<button v-if="isEndFlag" @click="sendsmscode" id="sendsmscode-yonghu" type="button" class="el-btn">发送验证码</button>
							<button v-if="!isEndFlag" disabled="disabled" type="button" class="el-btn">{{count}}秒后重发</button>
						</div>
					</el-form-item>
					<el-form-item v-if="tableName=='yonghu'" class="el-email list-item">
						<div class="label required">验证码：</div>
						<el-input v-model="smscode" type="text" id="smscode-yonghu" name="smscode" placeholder="请输入验证码" autocomplete="off" class="el-input" />
					</el-form-item>
					<el-form-item class="list-item" v-if="tableName=='shangjia'" prop="dianpumingcheng">
						<div class="label" :class="changeRules('dianpumingcheng')?'required':''">店铺名称：</div>
						<el-input v-model="registerForm.dianpumingcheng"  placeholder="请输入店铺名称" />
					</el-form-item>
					<el-form-item class="list-item" v-if="tableName=='shangjia'" prop="mima">
						<div class="label" :class="changeRules('mima')?'required':''">密码：</div>
						<el-input v-model="registerForm.mima" type="password" placeholder="请输入密码" />
					</el-form-item>
					<el-form-item class="list-item" v-if="tableName=='shangjia'" prop="mima2">
						<div class="label" :class="changeRules('mima')?'required':''">确认密码：</div>
						<el-input v-model="registerForm.mima2" type="password" placeholder="请再次输入密码" />
					</el-form-item>
					<el-form-item class="list-item" v-if="tableName=='shangjia'" prop="shangjiaxingming">
						<div class="label" :class="changeRules('shangjiaxingming')?'required':''">商家姓名：</div>
						<el-input v-model="registerForm.shangjiaxingming"  placeholder="请输入商家姓名" />
					</el-form-item>
					<el-form-item class="list-item" v-if="tableName=='shangjia'" prop="touxiang">
						<div class="label" :class="changeRules('touxiang')?'required':''">头像：</div>
						<file-upload
							tip="点击上传头像"
							action="file/upload"
							:limit="1"
							:multiple="true"
							:fileUrls="registerForm.touxiang?registerForm.touxiang:''"
							@change="shangjiatouxiangUploadChange"
						></file-upload>
					</el-form-item>
					<el-form-item class="list-item" v-if="tableName=='shangjia'" prop="xingbie">
						<div class="label" :class="changeRules('xingbie')?'required':''">性别：</div>
						<el-select v-model="registerForm.xingbie" placeholder="请选择性别" >
							<el-option
								v-for="(item,index) in shangjiaxingbieOptions"
								:key="index"
								:label="item"
								:value="item">
							</el-option>
						</el-select>
					</el-form-item>
					<el-form-item class="list-item" v-if="tableName=='shangjia'" prop="nianling">
						<div class="label" :class="changeRules('nianling')?'required':''">年龄：</div>
						<el-input v-model.number="registerForm.nianling"  placeholder="请输入年龄" />
					</el-form-item>
					<el-form-item class="list-item" v-if="tableName=='shangjia'" prop="youxiang">
						<div class="label" :class="changeRules('youxiang')?'required':''">邮箱：</div>
						<el-input v-model="registerForm.youxiang"  placeholder="请输入邮箱" />
					</el-form-item>
					<el-form-item v-if="tableName=='shangjia'" class="el-email list-item">
						<div class="label required">手机号：</div>
						<div style="display: flex;flex: 1;">
							<input v-model="registerForm.mobile" type="text" id="mobile-shangjia" name="mobile" placeholder="请输入手机号" autocomplete="off" class="emailInput">
							<button v-if="isEndFlag" @click="sendsmscode" id="sendsmscode-shangjia" type="button" class="el-btn">发送验证码</button>
							<button v-if="!isEndFlag" disabled="disabled" type="button" class="el-btn">{{count}}秒后重发</button>
						</div>
					</el-form-item>
					<el-form-item v-if="tableName=='shangjia'" class="el-email list-item">
						<div class="label required">验证码：</div>
						<el-input v-model="smscode" type="text" id="smscode-shangjia" name="smscode" placeholder="请输入验证码" autocomplete="off" class="el-input" />
					</el-form-item>
					<div class="register-btn">
						<div class="register-btn1">
							<el-button class="register_btn" type="primary" @click="submitForm('registerForm')">注册</el-button>
						</div>
						<div class="register-btn2">
							<router-link class="has_btn" to="/login">已有账号，直接登录</router-link>
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

export default {
    //数据集合
    data() {
		return {
            pageFlag : '',
			tableName: '',
			registerForm: {},
			forgetForm: {},
			rules: {},
			requiredRules: {},
            yonghuxingbieOptions: [],
            shangjiaxingbieOptions: [],
            smscode:'',
			//倒计时60s
			count: 60,
			//倒计时定时器
			inter: null,
			//倒计结束标识
			isEndFlag: true,
		}
    },
	mounted() {
		if(this.$route.query.pageFlag=='register'){
			this.tableName = this.$route.query.role;
			if(this.tableName=='yonghu'){
				this.registerForm = {
					yonghuming: '',
					mima: '',
					mima2: '',
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
				this.registerForm = {
					dianpumingcheng: '',
					mima: '',
					mima2: '',
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
				this.rules.yonghuming = [{ required: true, message: '请输入用户名', trigger: 'blur' }];
				this.requiredRules.yonghuming = [{ required: true, message: '请输入用户名', trigger: 'blur' }]
			}
			if ('yonghu' == this.tableName) {
				this.rules.mima = [{ required: true, message: '请输入密码', trigger: 'blur' }];
				this.requiredRules.mima = [{ required: true, message: '请输入密码', trigger: 'blur' }]
			}
			if ('yonghu' == this.tableName) {
				this.rules.xingming = [{ required: true, message: '请输入姓名', trigger: 'blur' }];
				this.requiredRules.xingming = [{ required: true, message: '请输入姓名', trigger: 'blur' }]
			}
			this.yonghuxingbieOptions = "男,女".split(',');
			if ('yonghu' == this.tableName) {
				this.rules.nianling = [{ required: true, validator: this.$validate.isIntNumer, trigger: 'blur' }];
			}
			if ('yonghu' == this.tableName) {
				this.rules.youxiang = [{ required: true, validator: this.$validate.isEmail, trigger: 'blur' }];
			}
			if ('yonghu' == this.tableName) {
				this.rules.mobile = [{ required: true, validator: this.$validate.isMobile, trigger: 'blur' }];
			}
			if ('yonghu' == this.tableName) {
				this.rules.money = [{ required: true, validator: this.$validate.isNumber, trigger: 'blur' }];
			}
			if ('shangjia' == this.tableName) {
				this.rules.dianpumingcheng = [{ required: true, message: '请输入店铺名称', trigger: 'blur' }];
				this.requiredRules.dianpumingcheng = [{ required: true, message: '请输入店铺名称', trigger: 'blur' }]
			}
			if ('shangjia' == this.tableName) {
				this.rules.mima = [{ required: true, message: '请输入密码', trigger: 'blur' }];
				this.requiredRules.mima = [{ required: true, message: '请输入密码', trigger: 'blur' }]
			}
			if ('shangjia' == this.tableName) {
				this.rules.shangjiaxingming = [{ required: true, message: '请输入商家姓名', trigger: 'blur' }];
				this.requiredRules.shangjiaxingming = [{ required: true, message: '请输入商家姓名', trigger: 'blur' }]
			}
			this.shangjiaxingbieOptions = "男,女".split(',');
			if ('shangjia' == this.tableName) {
				this.rules.nianling = [{ required: true, validator: this.$validate.isIntNumer, trigger: 'blur' }];
			}
			if ('shangjia' == this.tableName) {
				this.rules.youxiang = [{ required: true, validator: this.$validate.isEmail, trigger: 'blur' }];
			}
			if ('shangjia' == this.tableName) {
				this.rules.mobile = [{ required: true, validator: this.$validate.isMobile, trigger: 'blur' }];
			}
			if ('shangjia' == this.tableName) {
				this.rules.money = [{ required: true, validator: this.$validate.isNumber, trigger: 'blur' }];
			}
		}
	},
    created() {
		this.pageFlag = this.$route.query.pageFlag;
    },
    //方法集合
    methods: {
		changeRules(name){
			if(this.requiredRules[name]){
				return true
			}
			return false
		},
		// 获取uuid
		getUUID () {
			return new Date().getTime();
		},
        // 下二随
		yonghutouxiangUploadChange(fileUrls) {
			this.registerForm.touxiang = fileUrls.replace(new RegExp(this.$config.baseUrl,"g"),"");
		},
		shangjiatouxiangUploadChange(fileUrls) {
			this.registerForm.touxiang = fileUrls.replace(new RegExp(this.$config.baseUrl,"g"),"");
		},

		// 多级联动参数

		sendsmscode() {
			if(!this.registerForm.mobile){
				this.$message.error(`手机号码不能为空`);
				return
			}
			let mobileF = this.$validate.isMobile2(this.registerForm.mobile);
			if(this.registerForm.mobile&&(!this.$validate.isMobile2(this.registerForm.mobile))){
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
				url: `${this.tableName}/sendsms?mobile=`+this.registerForm.mobile,
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

		submitForm(formName) {
			this.$refs[formName].validate((valid) => {
				if (valid) {
					var url=this.tableName+"/register";
					if(`yonghu` == this.tableName ){
						url=this.tableName+"/register?smscode="+this.smscode;
						if(this.registerForm.mobile&&(!this.$validate.isMobile2(this.registerForm.mobile))){
							this.$message.error(`请输入正确的手机格式`);
							return
						}
					}
					if((!this.smscode) && `yonghu` == this.tableName){
						this.$message.error(`验证码不能为空`);
						return
					}
					if(`yonghu` == this.tableName && this.registerForm.mima!=this.registerForm.mima2) {
						this.$message.error(`两次密码输入不一致`);
						return
					}
					if(`shangjia` == this.tableName ){
						url=this.tableName+"/register?smscode="+this.smscode;
						if(this.registerForm.mobile&&(!this.$validate.isMobile2(this.registerForm.mobile))){
							this.$message.error(`请输入正确的手机格式`);
							return
						}
					}
					if((!this.smscode) && `shangjia` == this.tableName){
						this.$message.error(`验证码不能为空`);
						return
					}
					if(`shangjia` == this.tableName && this.registerForm.mima!=this.registerForm.mima2) {
						this.$message.error(`两次密码输入不一致`);
						return
					}
					this.$http.post(url, this.registerForm).then(res => {
						if (res.data.code === 0) {
							this.$message({
								message: '注册成功',
								type: 'success',
								duration: 1500,
								onClose: () => {
									this.$router.push('/login');
								}
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
		resetForm(formName) {
			this.$refs[formName].resetFields();
		}
    }
}
</script>

<style rel="stylesheet/scss" lang="scss" scoped>
	.container {
		background: #f5f5f5;
		display: flex;
		width: 100%;
		min-height: 100vh;
		justify-content: center;
		align-items: center;
		background-position: center center;
		position: relative;
		.rgs-form {
			padding: 0;
			margin: 0;
			background: rgba(255, 255, 255, 0.95);
			width: 500px;
			height: auto;
			border-radius: 20px;
			box-shadow: 0 20px 60px rgba(0, 0, 0, 0.3);
			backdrop-filter: blur(10px);
			.rgs-form2 {
				padding: 40px 50px;
				margin: 0;
				background: transparent;
				width: 100%;
				.title {
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
					::v-deep .el-form-item__content {
						padding: 0;
						display: flex;
						width: 100%;
						flex: 1;
						.label {
							color: #666;
							width: 80px;
							font-size: 15px;
							line-height: 50px;
							text-align: center;
							font-weight: 500;
						}
						
						.required {
							position: relative;
						}
						.required::after{
							margin: 0 5px 0 0;
							color: red;
							position: relative;
							content: "*";
						}
						.el-input {
							flex: 1;
							width: 100%;
						}
						.el-input .el-input__inner {
							border: 0px solid #ddd;
							padding: 0 15px;
							color: #333;
							background: transparent;
							width: 100%;
							font-size: 15px;
							height: 50px;
						}
						.el-input .el-input__inner:focus {
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
						.el-input-number {
							flex: 1;
							width: 100%;
						}
						.el-input-number .el-input__inner {
							text-align: left;
							border: 0px solid #ddd;
							padding: 0 15px;
							color: #333;
							background: transparent;
							width: 100%;
							font-size: 15px;
							height: 50px;
						}
						.el-input-number .el-input-number__decrease {
							display: none;
						}
						.el-input-number .el-input-number__increase {
							display: none;
						}
						.el-select {
							flex: 1;
							width: 100%;
						}
						.el-select .el-input__inner {
							border: none;
							padding: 0 15px;
							box-shadow: none;
							color: #333;
							background: transparent;
							width: 100%;
							font-size: 15px;
							height: 50px;
						}
						.el-select .is-focus .el-input__inner {
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
						.el-upload--picture-card {
							background: transparent;
							border: 0;
							border-radius: 0;
							width: auto;
							height: auto;
							line-height: initial;
							vertical-align: middle;
						}
						.upload .upload-img {
							border: 1px solid #ddd;
							cursor: pointer;
							border-radius: 8px;
							margin: 0;
							color: #999;
							background: #fff;
							object-fit: cover;
							width: 80px;
							font-size: 24px;
							line-height: 50px;
							text-align: center;
							height: 50px;
						}
						.el-upload-list .el-upload-list__item {
							border: 1px solid #ddd;
							cursor: pointer;
							border-radius: 8px;
							margin: 0;
							color: #999;
							background: #fff;
							object-fit: cover;
							width: 80px;
							font-size: 24px;
							line-height: 50px;
							text-align: center;
							height: 50px;
							font-size: 14px;
							line-height: 1.8;
						}
						.el-upload .el-icon-plus {
							border: 1px solid #ddd;
							cursor: pointer;
							border-radius: 8px;
							margin: 0;
							color: #999;
							background: #fff;
							object-fit: cover;
							width: 80px;
							font-size: 24px;
							line-height: 50px;
							text-align: center;
							height: 50px;
						}
						.el-upload__tip {
							margin: 0;
							color: #666;
							font-size: 14px;
						}
						.emailInput {
							border: 0px solid #ddd;
							padding: 0 15px;
							outline: none;
							color: #333;
							flex: 1;
							background: transparent;
							width: 100%;
							font-size: 15px;
							height: 50px;
						}
						.emailInput:focus {
							border: 0px solid #ddd;
							padding: 0 15px;
							color: #333;
							flex: 1;
							background: transparent;
							width: 100%;
							font-size: 15px;
							height: 50px;
						}
						.el-btn {
							border: none;
							cursor: pointer;
							border-radius: 0 12px 12px 0;
							padding: 0 15px;
							margin: 0;
							color: #fff;
							background: #5DB5B7;
							width: 110px;
							font-size: 14px;
							border-width: 1px 1px 1px 0;
							float: right;
							height: 50px;
							transition: all 0.3s ease;
						}
						.el-btn:hover {
							background: #4aa5a7;
						}
						
						.el-input__inner::placeholder {
							color: #bbb;
							font-size: 14px;
						}
						input::placeholder {
							color: #bbb;
							font-size: 14px;
						}
					}
				}
				.list-item:hover {
					border-color: #5DB5B7;
					box-shadow: 0 4px 12px rgba(93, 181, 183, 0.2);
				}
				.register-btn {
					margin: 0;
					flex-direction: column;
					display: flex;
					width: 100%;
				}
				.register-btn1 {
					padding: 0 0 20px 0;
					width: 100%;
				}
				.register-btn2 {
					padding: 0;
					margin: 0 auto;
					width: 100%;
					text-align: center;
				}
				.register_btn {
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
				.register_btn:hover {
					background: linear-gradient(135deg, #4aa5a7 0%, #2d8587 100%);
					transform: translateY(-2px);
					box-shadow: 0 12px 28px rgba(93, 181, 183, 0.4);
				}
				.has_btn {
					cursor: pointer;
					padding: 8px 20px;
					color: #5DB5B7;
					display: inline-block;
					text-decoration: none;
					font-size: 15px;
					border: 1px solid #5DB5B7;
					border-radius: 20px;
					transition: all 0.3s ease;
				}
				.has_btn:hover {
					color: #fff;
					background: #5DB5B7;
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
	
	::-webkit-scrollbar {
		display: none;
	}
</style>
