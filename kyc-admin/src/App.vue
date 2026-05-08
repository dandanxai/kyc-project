<template>
  <router-view />
</template>

<style>
/* 全局重置，确保后台系统占满全屏 */
html, body, #app {
  width: 100%;
  height: 100%;
  margin: 0;
  padding: 0;
  background-color: #0D0E12; /* 经典深邃暗黑底色 */
  overflow: hidden;
}
</style>