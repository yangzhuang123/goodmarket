<template>
	<div class="breadcrumb-container">
		<el-breadcrumb separator="/">
			<el-breadcrumb-item>
				<a @click.prevent="goHome">
					<i class="el-icon-s-home"></i>
					<span>首页</span>
				</a>
			</el-breadcrumb-item>
			<el-breadcrumb-item v-for="(item, index) in levelList" :key="item.path">
				<span v-if="item.redirect === 'noRedirect' || index === levelList.length - 1" class="current">
					{{ getTitle(item) }}
				</span>
				<a v-else @click.prevent="handleLink(item)">
					{{ getTitle(item) }}
				</a>
			</el-breadcrumb-item>
		</el-breadcrumb>
	</div>
</template>

<script>
export default {
	data() {
		return {
			levelList: []
		}
	},
	watch: {
		$route() {
			this.getBreadcrumb()
		}
	},
	created() {
		this.getBreadcrumb()
	},
	methods: {
		getTitle(item) {
			return (item.meta && item.meta.title) || item.name || ''
		},
		getBreadcrumb() {
			const route = this.$route
			const matched = route.matched.filter(function(item) {
				return item.meta && item.meta.title
			})
			this.levelList = matched
		},
		goHome() {
			this.$router.push('/')
		},
		handleLink(item) {
			const redirect = item.redirect
			const path = item.path
			if (redirect) {
				this.$router.push(redirect)
			} else if (path) {
				this.$router.push(path)
			}
		}
	}
}
</script>

<style lang="scss" scoped>
.breadcrumb-container {
	padding: 0;

	::v-deep .el-breadcrumb {
		display: flex;
		align-items: center;

		.el-breadcrumb__item {
			.el-breadcrumb__inner {
				display: flex;
				align-items: center;

				a {
					color: #606266;
					font-weight: 400;
					transition: color 0.3s ease;
					display: flex;
					align-items: center;
					gap: 4px;

					&:hover {
						color: #667eea;
					}

					i {
						font-size: 14px;
					}
				}

				.current {
					color: #303133;
					font-weight: 500;
				}
			}

			.el-breadcrumb__separator {
				color: #c0c4cc;
				margin: 0 8px;
			}

			&:last-child {
				.el-breadcrumb__inner {
					color: #303133;
				}
			}
		}
	}
}
</style>
