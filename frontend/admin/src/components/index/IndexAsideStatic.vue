<template>
	<div class="aside-container">
		<div class="aside-header">
			<div class="logo-wrapper">
				<div class="logo">
					<i class="el-icon-shopping-cart-2"></i>
				</div>
				<span class="logo-text">好物集市</span>
			</div>
		</div>

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
			<div class="user-details">
				<div class="user-name">{{ userName }}</div>
				<div class="user-role">{{ role }}</div>
			</div>
		</div>

		<el-scrollbar class="menu-scrollbar">
			<el-menu
				:default-active="activeMenu"
				:unique-opened="true"
				:collapse-transition="false"
				class="aside-menu"
			>
				<el-menu-item index="/" @click="menuHandler('')">
					<i class="el-icon-s-home"></i>
					<span slot="title">系统首页</span>
				</el-menu-item>

				<el-submenu index="user-center">
					<template slot="title">
						<i class="el-icon-user-solid"></i>
						<span>个人中心</span>
					</template>
					<el-menu-item index="/updatePassword" @click="menuHandler('updatePassword')">
						<i class="el-icon-lock"></i>
						<span>修改密码</span>
					</el-menu-item>
					<el-menu-item index="/center" @click="menuHandler('center')">
						<i class="el-icon-setting"></i>
						<span>个人信息</span>
					</el-menu-item>
				</el-submenu>

				<template v-for="(menu, index) in menuList.backMenu">
					<el-submenu 
						v-if="menu.child && menu.child.length > 1" 
						:key="index" 
						:index="'menu-' + index"
					>
						<template slot="title">
							<i :class="getMenuIcon(index)"></i>
							<span>{{ menu.menu }}</span>
						</template>
						<el-menu-item 
							v-for="(child, sort) in menu.child" 
							:key="sort" 
							:index="'/' + child.tableName"
							@click="menuHandler(child.tableName)"
						>
							<i class="el-icon-document"></i>
							<span>{{ child.menu }}</span>
						</el-menu-item>
					</el-submenu>

					<el-menu-item 
						v-else-if="menu.child && menu.child.length === 1"
						:key="'single-' + index"
						:index="'/' + menu.child[0].tableName"
						@click="menuHandler(menu.child[0].tableName)"
					>
						<i :class="getMenuIcon(index)"></i>
						<span slot="title">{{ menu.child[0].menu }}</span>
					</el-menu-item>
				</template>
			</el-menu>
		</el-scrollbar>
	</div>
</template>

<script>
import menu from '@/utils/menu'

export default {
	data() {
		return {
			menuList: [],
			role: '',
			user: null,
			avatar: '',
			userName: '',
			baseUrl: this.$base.url,
			icons: [
				'el-icon-goods',
				'el-icon-tickets',
				'el-icon-s-shop',
				'el-icon-s-order',
				'el-icon-s-marketing',
				'el-icon-s-platform',
				'el-icon-s-data',
				'el-icon-s-custom',
				'el-icon-s-management',
				'el-icon-s-finance',
				'el-icon-s-check',
				'el-icon-s-grid',
				'el-icon-message',
				'el-icon-chat-dot-square',
				'el-icon-picture',
				'el-icon-s-promotion',
				'el-icon-s-release',
				'el-icon-s-ticket',
			],
		}
	},
	computed: {
		activeMenu() {
			const route = this.$route
			const { meta, path } = route
			if (meta.activeMenu) {
				return meta.activeMenu
			}
			return path
		}
	},
	mounted() {
		this.initMenu()
		this.loadUserInfo()
	},
	methods: {
		getMenuIcon(index) {
			return this.icons[index % this.icons.length]
		},
		initMenu() {
			const menus = menu.list()
			if (menus) {
				this.menuList = menus
			}
			this.role = this.$storage.get('role')
			this.userName = this.$storage.get('adminName') || '管理员'

			for (let i = 0; i < this.menuList.length; i++) {
				if (this.menuList[i].roleName == this.role) {
					this.menuList = this.menuList[i]
					break
				}
			}
		},
		loadUserInfo() {
			const sessionTable = this.$storage.get("sessionTable")
			if (!sessionTable) return

			this.$http({
				url: sessionTable + '/session',
				method: "get"
			}).then(({ data }) => {
				if (data && data.code === 0) {
					this.user = data.data
					if (sessionTable == 'yonghu' || sessionTable == 'shangjia') {
						this.avatar = data.data.touxiang
					}
					if (sessionTable == 'users') {
						this.avatar = data.data.image
					}
				}
			})
		},
		menuHandler(name) {
			this.$router.push('/' + name)
		}
	}
}
</script>

<style lang="scss" scoped>
.aside-container {
	width: 240px;
	height: 100%;
	background: linear-gradient(180deg, #0d1117 0%, #161b22 50%, #0d1117 100%);
	display: flex;
	flex-direction: column;
	transition: width 0.3s ease;
	position: relative;
	overflow: hidden;
	box-sizing: border-box;
	border: none;
	outline: none;

	.aside-header {
		padding: 24px 20px;
		border-bottom: 1px solid rgba(255, 255, 255, 0.08);

		.logo-wrapper {
			display: flex;
			align-items: center;
			gap: 12px;

			.logo {
				width: 42px;
				height: 42px;
				background: linear-gradient(135deg, #58a6ff 0%, #0969da 100%);
				border-radius: 12px;
				display: flex;
				align-items: center;
				justify-content: center;
				box-shadow: 0 4px 15px rgba(88, 166, 255, 0.4);

				i {
					font-size: 22px;
					color: #fff;
				}
			}

			.logo-text {
				font-size: 18px;
				font-weight: 600;
				color: #f0f6fc;
				white-space: nowrap;
			}
		}

		.logo-mini {
			display: flex;
			justify-content: center;

			i {
				font-size: 28px;
				color: #f0f6fc;
				background: linear-gradient(135deg, #58a6ff 0%, #0969da 100%);
				-webkit-background-clip: text;
				-webkit-text-fill-color: transparent;
			}
		}
	}

	.user-info {
		padding: 20px;
		display: flex;
		align-items: center;
		gap: 12px;
		border-bottom: 1px solid rgba(255, 255, 255, 0.08);

		.user-avatar {
			width: 48px;
			height: 48px;
			border-radius: 50%;
			overflow: hidden;
			border: 2px solid rgba(88, 166, 255, 0.5);
			flex-shrink: 0;

			.el-image {
				width: 100%;
				height: 100%;
			}

			.avatar-placeholder {
				width: 100%;
				height: 100%;
				background: linear-gradient(135deg, #58a6ff 0%, #0969da 100%);
				display: flex;
				align-items: center;
				justify-content: center;

				i {
					font-size: 24px;
					color: #f0f6fc;
				}
			}
		}

		.user-details {
			flex: 1;
			min-width: 0;

			.user-name {
				font-size: 15px;
				font-weight: 500;
				color: #f0f6fc;
				white-space: nowrap;
				overflow: hidden;
				text-overflow: ellipsis;
			}

			.user-role {
				font-size: 12px;
				color: rgba(240, 246, 252, 0.6);
				margin-top: 4px;
			}
		}
	}

	.menu-scrollbar {
		flex: 1;
		overflow: hidden;

		::v-deep .el-scrollbar__wrap {
			overflow-x: hidden;
		}
	}

	.aside-menu {
		border: none;
		background: transparent;
		padding: 10px 0;

		::v-deep .el-menu-item,
		::v-deep .el-submenu__title {
			height: 50px;
			line-height: 50px;
			color: rgba(240, 246, 252, 0.75);
			padding: 0 20px !important;
			margin: 4px 12px;
			border-radius: 8px;
			transition: all 0.3s ease;

			i {
				color: inherit;
				margin-right: 12px;
				font-size: 18px;
			}

			&:hover {
				color: #f0f6fc;
				background: rgba(88, 166, 255, 0.15);
			}
		}

		::v-deep .el-menu-item.is-active {
			color: #f0f6fc;
			background: linear-gradient(90deg, #58a6ff 0%, #0969da 100%);
			box-shadow: 0 4px 15px rgba(88, 166, 255, 0.4);

			i {
				color: #f0f6fc;
			}
		}

		::v-deep .el-submenu {
			.el-menu {
				background: transparent;
				padding-left: 0;

				.el-menu-item {
					height: 44px;
					line-height: 44px;
					padding-left: 52px !important;
					font-size: 14px;

					i {
						font-size: 16px;
						margin-right: 10px;
					}

					&:hover {
						background: rgba(88, 166, 255, 0.1);
					}

					&.is-active {
						background: rgba(88, 166, 255, 0.25);
						box-shadow: none;
					}
				}
			}
		}

		::v-deep .el-submenu.is-active > .el-submenu__title {
			color: #f0f6fc;

			i {
				color: #f0f6fc;
			}
		}
	}
}

::v-deep .el-menu--vertical .el-menu--popup {
	background: #161b22 !important;
	border: 1px solid rgba(255, 255, 255, 0.08);
	border-radius: 8px;
	padding: 8px 0;
	min-width: 160px;

	.el-menu-item {
		color: rgba(240, 246, 252, 0.75) !important;
		background: transparent !important;
		height: 44px !important;
		line-height: 44px !important;
		padding: 0 20px !important;

		&:hover {
			color: #f0f6fc !important;
			background: rgba(88, 166, 255, 0.15) !important;
		}

		&.is-active {
			color: #f0f6fc !important;
			background: linear-gradient(90deg, #58a6ff 0%, #0969da 100%) !important;
		}

		i {
			color: inherit;
			margin-right: 10px;
		}
	}
}
</style>
