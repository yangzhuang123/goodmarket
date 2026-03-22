<template>
	<div class="header-container">
		<div class="header-left">
			<div class="page-title">{{ pageTitle }}</div>
		</div>
		<div class="header-right">
			<el-dropdown class="user-dropdown" @command="handleCommand" trigger="click">
				<div class="user-info">
					<div class="user-avatar">
						<el-image 
							v-if="avatar" 
							:src="baseUrl + avatar" 
							fit="cover"
						>
							<div slot="error" class="avatar-placeholder">
								<i class="el-icon-user"></i>
							</div>
						</el-image>
						<div v-else class="avatar-placeholder">
							<i class="el-icon-user"></i>
						</div>
					</div>
					<span class="user-name">{{ userName }}</span>
					<i class="el-icon-arrow-down"></i>
				</div>
				<el-dropdown-menu slot="dropdown" class="header-dropdown-menu">
					<el-dropdown-item command="">
						<i class="el-icon-s-home"></i>
						<span>系统首页</span>
					</el-dropdown-item>
					<el-dropdown-item command="center">
						<i class="el-icon-user"></i>
						<span>个人中心</span>
					</el-dropdown-item>
					<el-dropdown-item v-if="role !== '管理员'" command="front">
						<i class="el-icon-monitor"></i>
						<span>前台首页</span>
					</el-dropdown-item>
					<el-dropdown-item v-if="role === '管理员'" command="backUp">
						<i class="el-icon-download"></i>
						<span>数据备份</span>
					</el-dropdown-item>
					<el-dropdown-item command="logout" divided>
						<i class="el-icon-switch-button"></i>
						<span>退出登录</span>
					</el-dropdown-item>
				</el-dropdown-menu>
			</el-dropdown>
		</div>
	</div>
</template>

<script>
export default {
	data() {
		return {
			user: null,
			baseUrl: this.$base.url,
		}
	},
	computed: {
		userName() {
			return this.$storage.get('adminName') || '管理员'
		},
		role() {
			return this.$storage.get('role') || ''
		},
		avatar() {
			return this.$storage.get('headportrait') || ''
		},
		pageTitle() {
			return this.$route.meta?.title || this.$project.projectName || '好物集市'
		}
	},
	mounted() {
		this.loadUserInfo()
	},
	methods: {
		loadUserInfo() {
			const sessionTable = this.$storage.get("sessionTable")
			if (!sessionTable) return

			this.$http({
				url: sessionTable + '/session',
				method: "get"
			}).then(({ data }) => {
				if (data && data.code === 0) {
					this.user = data.data
					if (sessionTable === 'yonghu' || sessionTable === 'shangjia') {
						this.$storage.set('headportrait', data.data.touxiang)
					}
				}
			})
		},
		handleCommand(name) {
			if (name === 'logout') {
				this.onLogout()
			} else if (name === 'front') {
				this.onIndexTap()
			} else if (name === 'backUp') {
				this.backUp()
			} else {
				this.$router.push('/' + name)
			}
		},
		onLogout() {
			this.$confirm('确定要退出登录吗?', '提示', {
				confirmButtonText: '确定',
				cancelButtonText: '取消',
				type: 'warning'
			}).then(() => {
				this.$storage.clear()
				this.$store.dispatch('tagsView/delAllViews')
				this.$router.replace({ name: "login" })
			}).catch(() => {})
		},
		onIndexTap() {
			window.location.href = this.$base.indexUrl
		},
		backUp() {
			this.$confirm('是否备份数据库?', '数据备份提示', {
				confirmButtonText: '确定',
				cancelButtonText: '取消',
				type: 'info'
			}).then(() => {
				this.$http({
					url: '/mysqldump',
					method: "get"
				}).then(({ data }) => {
					if (data) {
						const binaryData = []
						binaryData.push(data)
						const objectUrl = window.URL.createObjectURL(new Blob(binaryData, {
							type: 'application/pdf;chartset=UTF-8'
						}))
						const a = document.createElement('a')
						a.href = objectUrl
						a.download = 'mysql.dmp'
						a.click()
						window.URL.revokeObjectURL(objectUrl)
					}
				})
			}).catch(() => {})
		}
	}
}
</script>

<style lang="scss" scoped>
.header-container {
	height: 60px;
	padding: 0 24px;
	display: flex;
	align-items: center;
	justify-content: space-between;
	background: #fff;
	border-bottom: 1px solid #f0f0f0;

	.header-left {
		.page-title {
			font-size: 18px;
			font-weight: 600;
			color: #1a1a2e;
		}
	}

	.header-right {
		display: flex;
		align-items: center;

		.user-dropdown {
			cursor: pointer;

			.user-info {
				display: flex;
				align-items: center;
				gap: 10px;
				padding: 6px 12px;
				border-radius: 24px;
				transition: background 0.3s ease;

				&:hover {
					background: #f5f7fa;
				}

				.user-avatar {
					width: 36px;
					height: 36px;
					border-radius: 50%;
					overflow: hidden;
					border: 2px solid #e4e7ed;

					.el-image {
						width: 100%;
						height: 100%;
					}

					.avatar-placeholder {
						width: 100%;
						height: 100%;
						background: linear-gradient(135deg, #667eea 0%, #764ba2 100%);
						display: flex;
						align-items: center;
						justify-content: center;

						i {
							font-size: 18px;
							color: #fff;
						}
					}
				}

				.user-name {
					font-size: 14px;
					font-weight: 500;
					color: #303133;
					max-width: 100px;
					overflow: hidden;
					text-overflow: ellipsis;
					white-space: nowrap;
				}

				.el-icon-arrow-down {
					font-size: 12px;
					color: #909399;
					transition: transform 0.3s ease;
				}
			}
		}
	}
}

.header-dropdown-menu {
	margin-top: 8px;
	padding: 8px 0;
	border-radius: 8px;
	border: 1px solid #e4e7ed;
	box-shadow: 0 4px 20px rgba(0, 0, 0, 0.1);

	::v-deep .el-dropdown-menu__item {
		display: flex;
		align-items: center;
		gap: 10px;
		padding: 10px 20px;
		font-size: 14px;
		color: #606266;
		transition: all 0.3s ease;

		i {
			font-size: 16px;
			color: #909399;
		}

		&:hover {
			background: linear-gradient(90deg, rgba(102, 126, 234, 0.1) 0%, rgba(118, 75, 162, 0.1) 100%);
			color: #667eea;

			i {
				color: #667eea;
			}
		}

		&.is-divided {
			border-top: 1px solid #e4e7ed;
			margin-top: 8px;
			padding-top: 16px;
		}
	}
}
</style>
