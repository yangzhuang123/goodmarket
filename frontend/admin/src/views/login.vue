<template>
	<div class="login-container">
		<div class="login-left">
			<div class="login-left-content">
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
		<div class="login-right">
			<div class="login-form-wrapper animate__animated animate__fadeInRight">
				<div class="form-header">
					<h2>欢迎登录</h2>
					<p>请输入您的账号和密码</p>
				</div>
				<el-form class="login-form" :model="rulesForm">
					<div class="form-group">
						<label>账号</label>
						<div class="input-wrapper">
							<i class="el-icon-user"></i>
							<input 
								placeholder="请输入账号" 
								name="username" 
								type="text" 
								v-model="rulesForm.username"
								@keyup.enter="login()"
							>
						</div>
					</div>
					<div class="form-group">
						<label>密码</label>
						<div class="input-wrapper">
							<i class="el-icon-lock"></i>
							<input 
								placeholder="请输入密码" 
								name="password" 
								:type="showPassword ? 'text' : 'password'" 
								v-model="rulesForm.password"
								@keyup.enter="login()"
							>
							<i 
								class="toggle-password" 
								:class="showPassword ? 'el-icon-view' : 'el-icon-hide'"
								@click="showPassword = !showPassword"
							></i>
						</div>
					</div>

					<div class="form-group" v-if="roles.length > 1">
						<label>角色</label>
						<div class="role-list">
							<el-radio 
								v-for="item in roles" 
								:key="item.roleName" 
								v-model="rulesForm.role" 
								:label="item.roleName"
							>{{ item.roleName }}</el-radio>
						</div>
					</div>

					<div class="form-actions">
						<el-button 
							type="primary" 
							@click="login()" 
							class="login-btn"
							:loading="loading"
						>
							<span v-if="!loading">登 录</span>
							<span v-else>登录中...</span>
						</el-button>
					</div>

					<div class="form-footer">
						<span>还没有账号？</span>
						<el-button type="text" @click="register('shangjia')">注册商家</el-button>
					</div>
				</el-form>
			</div>
		</div>
	</div>
</template>

<script>
import 'animate.css'
import menu from "@/utils/menu";

export default {
	data() {
		return {
			baseUrl: this.$base.url,
			loginType: 1,
			rulesForm: {
				username: "",
				password: "",
				role: "",
			},
			menus: [],
			roles: [],
			tableName: "",
			showPassword: false,
			loading: false,
		};
	},
	mounted() {
		let menus = menu.list();
		this.menus = menus;
		for (let i = 0; i < this.menus.length; i++) {
			if (this.menus[i].hasBackLogin == '是') {
				this.roles.push(this.menus[i])
			}
		}
	},
	methods: {
		register(tableName) {
			this.$storage.set("loginTable", tableName);
			this.$router.push({ path: '/register', query: { pageFlag: 'register' } })
		},
		login() {
			if (!this.rulesForm.username) {
				this.$message.error("请输入用户名");
				return;
			}
			if (!this.rulesForm.password) {
				this.$message.error("请输入密码");
				return;
			}
			if (this.roles.length > 1) {
				if (!this.rulesForm.role) {
					this.$message.error("请选择角色");
					return;
				}
				let menus = this.menus;
				for (let i = 0; i < menus.length; i++) {
					if (menus[i].roleName == this.rulesForm.role) {
						this.tableName = menus[i].tableName;
					}
				}
			} else {
				this.tableName = this.roles[0].tableName;
				this.rulesForm.role = this.roles[0].roleName;
			}
			this.loginPost()
		},
		loginPost() {
			this.loading = true;
			this.$http({
				url: `${this.tableName}/login?username=${this.rulesForm.username}&password=${this.rulesForm.password}`,
				method: "post"
			}).then(({ data }) => {
				this.loading = false;
				if (data && data.code === 0) {
					this.$storage.set("Token", data.token);
					this.$storage.set("role", this.rulesForm.role);
					this.$storage.set("sessionTable", this.tableName);
					this.$storage.set("adminName", this.rulesForm.username);
					this.$message.success("登录成功");
					this.$router.replace({ path: "/" });
				} else {
					this.$message.error(data.msg);
				}
			}).catch(() => {
				this.loading = false;
				this.$message.error("登录失败，请稍后重试");
			});
		},
	}
}
</script>

<style lang="scss" scoped>
.login-container {
	display: flex;
	min-height: 100vh;
	width: 100%;
}

.login-left {
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

	.login-left-content {
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

.login-right {
	flex: 1;
	display: flex;
	align-items: center;
	justify-content: center;
	background: #f8fafc;
	padding: 40px;

	.login-form-wrapper {
		width: 100%;
		max-width: 400px;
		background: #fff;
		border-radius: 16px;
		padding: 48px 40px;
		box-shadow: 0 20px 60px rgba(0,0,0,0.1);

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

		.login-form {
			.form-group {
				margin-bottom: 24px;

				label {
					display: block;
					font-size: 14px;
					font-weight: 500;
					color: #374151;
					margin-bottom: 8px;
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

					input {
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

					.toggle-password {
						position: absolute;
						right: 16px;
						color: #9ca3af;
						cursor: pointer;
						font-size: 18px;
						transition: color 0.3s;

						&:hover {
							color: #667eea;
						}
					}
				}

				.role-list {
					display: flex;
					flex-wrap: wrap;
					gap: 16px;
					padding: 12px 0;

					::v-deep .el-radio {
						margin-right: 0;

						.el-radio__input {
							.el-radio__inner {
								width: 18px;
								height: 18px;
								border-color: #d1d5db;

								&:hover {
									border-color: #667eea;
								}
							}

							&.is-checked .el-radio__inner {
								border-color: #667eea;
								background: #667eea;
							}
						}

						.el-radio__label {
							color: #4b5563;
							font-size: 14px;
						}

						&.is-checked .el-radio__label {
							color: #667eea;
						}
					}
				}
			}

			.form-actions {
				margin-top: 32px;

				.login-btn {
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
	.login-container {
		flex-direction: column;
	}

	.login-left {
		padding: 60px 20px;

		.login-left-content {
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

	.login-right {
		padding: 30px 20px;

		.login-form-wrapper {
			padding: 32px 24px;
		}
	}
}
</style>
