<template>
	<div class="main-container">
		<index-aside 
			class="aside-wrapper"
		></index-aside>
		<el-main class="main-content">
			<index-header class="header-wrapper"></index-header>
			<bread-crumbs class="breadcrumb-wrapper"></bread-crumbs>
			<tags-view />
			<router-view class="router-view"></router-view>
		</el-main>
	</div>
</template>

<script>
import IndexAside from '@/components/index/IndexAsideStatic'
import IndexHeader from '@/components/index/IndexHeader'
import TagsView from '@/components/index/TagsView'

export default {
	components: {
		IndexAside,
		IndexHeader,
		TagsView
	}
}
</script>

<style lang="scss" scoped>
.main-container {
	height: 100%;
	margin: 0;
	padding: 0;
	box-sizing: border-box;
}

.aside-wrapper {
	position: fixed;
	top: 0;
	left: 0;
	height: 100%;
	z-index: 1001;
}

.main-content {
	position: relative;
	min-height: 100vh;
	padding: 0 !important;
	margin-left: 240px;
	background: #f0f2f5;
	display: flex;
	flex-direction: column;
	transition: margin-left 0.3s ease;
	box-sizing: border-box;
	z-index: 1000;
	border: none;
	outline: none;
	width: calc(100% - 240px);

	.header-wrapper {
		background: #fff;
		box-shadow: 0 2px 8px rgba(0, 0, 0, 0.08);
		padding: 0 24px;
		display: flex;
		align-items: center;
		justify-content: flex-end;
		height: 60px;
		position: sticky;
		top: 0;
		z-index: 100;
	}

	.breadcrumb-wrapper {
		padding: 16px 24px;
		background: #fff;
		border-bottom: 1px solid #f0f0f0;
	}

	.router-view {
		flex: 1;
		padding: 0;
		overflow-y: auto;
		background: #f0f0f0;
		position: relative;
		z-index: 1000;
		box-sizing: border-box;
	}
}

// 覆盖Element UI的el-main默认样式
::v-deep .el-main {
	padding: 0 !important;
	box-sizing: border-box !important;
	background: #f0f2f5 !important;
}
</style>
