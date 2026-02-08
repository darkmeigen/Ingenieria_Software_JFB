<template>
  <aside class="sidebar">
    <div class="sidebar-header">
      <div class="logo">
        <span class="logo-icon">
             <!-- Logo SVG -->
            <svg width="30" height="30" viewBox="0 0 24 24" fill="none" xmlns="http://www.w3.org/2000/svg">
                <path d="M12 2L2 7L12 12L22 7L12 2Z" stroke="#4CAF50" stroke-width="2" stroke-linecap="round" stroke-linejoin="round"/>
                <path d="M2 17L12 22L22 17" stroke="#4CAF50" stroke-width="2" stroke-linecap="round" stroke-linejoin="round"/>
                <path d="M2 12L12 17L22 12" stroke="#4CAF50" stroke-width="2" stroke-linecap="round" stroke-linejoin="round"/>
            </svg>
        </span>
        <!-- Dynamic Title -->
        <span>Inicio > {{ currentModule }}</span>
      </div>
    </div>
    
    <nav class="sidebar-nav">
      <router-link to="/facturar" class="nav-item">
        <!-- Ventas Icon (Cart) -->
        <svg class="icon" viewBox="0 0 24 24" fill="none" stroke="currentColor" stroke-width="2">
            <circle cx="9" cy="21" r="1"></circle>
            <circle cx="20" cy="21" r="1"></circle>
            <path d="M1 1h4l2.68 13.39a2 2 0 0 0 2 1.61h9.72a2 2 0 0 0 2-1.61L23 6H6"></path>
        </svg>
        Ventas
      </router-link>
      <router-link to="/productos" class="nav-item">
        <!-- Inventario Icon (Box) -->
        <svg class="icon" viewBox="0 0 24 24" fill="none" stroke="currentColor" stroke-width="2">
            <path d="M21 16V8a2 2 0 0 0-1-1.73l-7-4a2 2 0 0 0-2 0l-7 4A2 2 0 0 0 3 8v8a2 2 0 0 0 1 1.73l7 4a2 2 0 0 0 2 0l7-4A2 2 0 0 0 21 16z"></path>
            <polyline points="3.27 6.96 12 12.01 20.73 6.96"></polyline>
            <line x1="12" y1="22.08" x2="12" y2="12"></line>
        </svg>
        Inventario
      </router-link>
      <router-link to="/clientes" class="nav-item">
        <!-- Clientes Icon (Users) -->
        <svg class="icon" viewBox="0 0 24 24" fill="none" stroke="currentColor" stroke-width="2">
            <path d="M17 21v-2a4 4 0 0 0-4-4H5a4 4 0 0 0-4 4v2"></path>
            <circle cx="9" cy="7" r="4"></circle>
            <path d="M23 21v-2a4 4 0 0 0-3-3.87"></path>
            <path d="M16 3.13a4 4 0 0 1 0 7.75"></path>
        </svg>
        Clientes
      </router-link>
      
      <!-- Placeholder items using simple SVGs -->
      <a href="#" class="nav-item">
         <svg class="icon" viewBox="0 0 24 24" fill="none" stroke="currentColor" stroke-width="2"><rect x="3" y="3" width="18" height="18" rx="2" ry="2"></rect></svg>
         Compras
      </a>
      <a href="#" class="nav-item">
         <svg class="icon" viewBox="0 0 24 24" fill="none" stroke="currentColor" stroke-width="2"><path d="M12 1v22M17 5H9.5a3.5 3.5 0 0 0 0 7h5a3.5 3.5 0 0 1 0 7H6"></path></svg>
         Contabilidad
      </a>
      
      <a href="#" class="nav-item" @click.prevent="logout">
          <svg class="icon" viewBox="0 0 24 24" fill="none" stroke="currentColor" stroke-width="2"><path d="M9 21H5a2 2 0 0 1-2-2V5a2 2 0 0 1 2-2h4"></path><polyline points="16 17 21 12 16 7"></polyline><line x1="21" y1="12" x2="9" y2="12"></line></svg>
          Bloquear Menu (Salir)
      </a>
    </nav>
  </aside>
</template>

<script setup>
import { computed } from 'vue';
import { useRouter, useRoute } from 'vue-router';

const router = useRouter();
const route = useRoute();

const currentModule = computed(() => {
    switch(route.path) {
        case '/facturar': return 'Ventas';
        case '/productos': return 'Inventario';
        case '/clientes': return 'Clientes';
        default: return 'Nomina';
    }
});

const logout = () => {
  localStorage.removeItem('user');
  router.push('/login');
};
</script>

<style scoped>
.sidebar {
  width: 250px;
  background: #f8f9fa;
  height: 100vh;
  display: flex;
  flex-direction: column;
  border-right: 1px solid #dee2e6;
  position: fixed;
  left: 0;
  top: 0;
}

.sidebar-header {
  padding: 20px;
  background: white;
  border-bottom: 2px solid #eee;
}

.logo {
  display: flex;
  align-items: center;
  gap: 10px;
  font-weight: bold;
  color: #555;
}

.sidebar-nav {
  display: flex;
  flex-direction: column;
  padding-top: 10px;
}

.nav-item {
  display: flex;
  align-items: center;
  padding: 12px 20px;
  text-decoration: none;
  color: #333;
  font-size: 1rem;
  transition: background 0.3s;
}

.nav-item:hover {
  background: #e9ecef;
}

.nav-item.router-link-active {
  background: #4CAF50; /* Green from the image */
  color: white;
}

.icon {
  margin-right: 15px;
  width: 20px;
  text-align: center;
}
</style>
