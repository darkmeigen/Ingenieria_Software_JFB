<template>
  <div class="app-layout">
    <Sidebar />
    <main class="main-content">
      <router-view />
    </main>
  </div>
</template>

<script setup>
import Sidebar from './Sidebar.vue';
</script>

<style scoped>
.app-layout {
  display: flex;
}

.main-content {
  margin-left: 250px; /* Width of sidebar */
  padding: 20px;
  width: 100%;
  background: #f4f6f9;
  min-height: 100vh;
}
</style>
