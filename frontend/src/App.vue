<script setup>
import { RouterLink, RouterView } from 'vue-router'
</script>

<template>
  <header>
    <nav class="navbar">
      <div class="brand">
        <span class="logo-icon">:D</span>
        <span class="logo-text">Sistema Contable <strong>JFB</strong></span>
      </div>
      <div class="menu">
        <RouterLink to="/">Inicio</RouterLink>
        <RouterLink to="/login">Acceso</RouterLink>
        <RouterLink to="/facturar" class="highlight">Facturación</RouterLink>
      </div>
    </nav>
  </header>

  <main>
    <RouterView />
  </main>
</template>

<style scoped>
header {
  background-color: #ffffff;
  box-shadow: 0 2px 4px rgba(0,0,0,0.05);
  position: sticky;
  top: 0;
  z-index: 100;
}

.navbar {
  max-width: 1100px;
  margin: 0 auto;
  display: flex;
  justify-content: space-between;
  align-items: center;
  padding: 1rem 20px;
}

.brand { display: flex; align-items: center; gap: 10px; font-size: 1.2rem; color: var(--primary); }

.menu a {
  text-decoration: none;
  color: #666;
  margin-left: 20px;
  font-weight: 500;
  transition: color 0.3s;
}

.menu a:hover, .router-link-active { color: var(--primary); }

.highlight {
  background-color: var(--primary);
  color: white !important;
  padding: 8px 15px;
  border-radius: 4px;
}

.highlight:hover { background-color: var(--secondary); }
</style>