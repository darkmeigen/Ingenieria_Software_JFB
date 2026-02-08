<script setup>
import { RouterView } from 'vue-router'
</script>

<template>
  <RouterView />
</template>

<style>
/* Global Styles */
:root {
  --primary: #4CAF50;
  --secondary: #2E7D32;
  --accent: #FFC107;
  --text: #333;
  --bg: #f4f6f9;
}

body {
  margin: 0;
  font-family: 'Segoe UI', Tahoma, Geneva, Verdana, sans-serif;
  background-color: var(--bg);
  color: var(--text);
}

.card {
  background: white;
  border-radius: 8px;
  padding: 20px;
  box-shadow: 0 2px 4px rgba(0,0,0,0.05);
}

.btn {
  padding: 10px 20px;
  border: none;
  border-radius: 4px;
  cursor: pointer;
  font-weight: 500;
  transition: 0.3s;
}

.btn-primary { background: var(--primary); color: white; }
.btn-primary:hover { background: var(--secondary); }

input, select {
  width: 100%;
  padding: 10px;
  border: 1px solid #ddd;
  border-radius: 4px;
  margin-bottom: 10px;
}
</style>