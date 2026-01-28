<template>
  <div class="dashboard">
    <h1>管理面板</h1>
    <nav>
      <NuxtLink to="/dashboard/students">学生管理</NuxtLink>
      <NuxtLink to="/dashboard/psychology">学生心理管理</NuxtLink>
      <NuxtLink to="/dashboard/risk">心理风险管理</NuxtLink>
    </nav>
    <NuxtPage />
  </div>
</template>

<script setup lang="ts">
// 可根据需要添加逻辑
</script>

<style scoped>
.dashboard {
  max-width: 800px;
  margin: 2rem auto;
  padding: 2rem;
  background: #fff;
  border-radius: 8px;
  box-shadow: 0 2px 8px #eee;
}
nav {
  display: flex;
  gap: 2rem;
  margin-bottom: 2rem;
}
nav a {
  color: #42b983;
  font-weight: bold;
  text-decoration: none;
}
nav a.router-link-exact-active {
  text-decoration: underline;
}
</style>
