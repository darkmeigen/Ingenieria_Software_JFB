<template>
  <aside class="sidebar">
    <div class="sidebar-header">
      <router-link to="/" class="logo-link">
          <div class="logo-container">
            <img src="/AJM.jpeg" alt="Inicio" class="logo-img" />
          </div>
      </router-link>
    </div>
    
    <nav class="sidebar-nav">
      <!-- Ventas with Submenu -->
      <div class="nav-group">
        <a href="#" class="nav-item" @click.prevent="toggleVentas" :class="{ 'router-link-active': isVentasActive }">
            <svg class="icon" viewBox="0 0 24 24" fill="none" stroke="currentColor" stroke-width="2">
                <circle cx="9" cy="21" r="1"/><circle cx="20" cy="21" r="1"/>
                <path d="M1 1h4l2.68 13.39a2 2 0 0 0 2 1.61h9.72a2 2 0 0 0 2-1.61L23 6H6"/>
            </svg>
            Ventas
            <span class="arrow" :class="{ 'down': ventasOpen }">▼</span>
        </a>
        <div class="submenu" v-if="ventasOpen">
            <router-link to="/facturar" class="sub-item">Dashboard</router-link>
            <router-link to="/facturar/nueva" class="sub-item">Generar Factura</router-link>
            <router-link to="/facturar/historico" class="sub-item">Histórico</router-link>
        </div>
      </div>

      <!-- Products with Submenu -->
      <div class="nav-group">
        <a href="#" class="nav-item" @click.prevent="toggleProductos" :class="{ 'router-link-active': isProductosActive }">
            <svg class="icon" viewBox="0 0 24 24" fill="none" stroke="currentColor" stroke-width="2">
                <path d="M21 16V8a2 2 0 0 0-1-1.73l-7-4a2 2 0 0 0-2 0l-7 4A2 2 0 0 0 3 8v8a2 2 0 0 0 1 1.73l7 4a2 2 0 0 0 2 0l7-4A2 2 0 0 0 21 16z"></path>
                <polyline points="3.27 6.96 12 12.01 20.73 6.96"></polyline>
                <line x1="12" y1="22.08" x2="12" y2="12"></line>
            </svg>
            Productos
            <span class="arrow" :class="{ 'down': productosOpen }">▼</span>
        </a>
        <div class="submenu" v-if="productosOpen">
            <router-link to="/productos" class="sub-item">Dashboard</router-link>
            <router-link to="/productos/inventario" class="sub-item">Gestionar Inventario</router-link>
        </div>
      </div>

      <router-link to="/clientes" class="nav-item">
        <svg class="icon" viewBox="0 0 24 24" fill="none" stroke="currentColor" stroke-width="2">
            <path d="M17 21v-2a4 4 0 0 0-4-4H5a4 4 0 0 0-4 4v2"></path>
            <circle cx="9" cy="7" r="4"></circle>
            <path d="M23 21v-2a4 4 0 0 0-3-3.87"></path>
            <path d="M16 3.13a4 4 0 0 1 0 7.75"></path>
        </svg>
        Clientes
      </router-link>

      <a href="#" class="nav-item logout" @click.prevent="logout">
          <svg class="icon" viewBox="0 0 24 24" fill="none" stroke="currentColor" stroke-width="2"><path d="M9 21H5a2 2 0 0 1-2-2V5a2 2 0 0 1 2-2h4"></path><polyline points="16 17 21 12 16 7"></polyline><line x1="21" y1="12" x2="9" y2="12"></line></svg>
          Cerrar Sesión
      </a>
    </nav>
  </aside>
</template>

<script setup>
import { computed, ref } from 'vue';
import { useRouter, useRoute } from 'vue-router';

const router = useRouter();
const route = useRoute();
const ventasOpen = ref(true);
const productosOpen = ref(true);

const toggleVentas = () => {
    ventasOpen.value = !ventasOpen.value;
};

const toggleProductos = () => {
    productosOpen.value = !productosOpen.value;
};

const currentModule = computed(() => {
    const p = route.path;
    if (p.includes('/facturar')) return 'Ventas';
    if (p.includes('/productos')) return 'Productos';
    if (p.includes('/clientes')) return 'Clientes';
    return 'Nomina';
});

const isVentasActive = computed(() => route.path.includes('/facturar'));
const isProductosActive = computed(() => route.path.includes('/productos'));

const logout = () => {
  localStorage.removeItem('user');
  router.push('/login');
};
</script>

<style scoped>
.sidebar {
  width: 250px;
  background: white;
  height: 100vh;
  border-right: 1px solid #eee;
  display: flex;
  flex-direction: column;
}

.sidebar-header {
  padding: 20px;
  border-bottom: 1px solid #f0f0f0;
  text-align: center;
}

.logo-link {
    display: inline-block;
    cursor: pointer;
}

.logo-container {
    width: 80px;
    height: 80px;
    margin: 0 auto;
    border-radius: 50%;
    overflow: hidden;
    border: 2px solid #eee;
    transition: transform 0.2s;
}

.logo-container:hover {
    transform: scale(1.05);
    border-color: #4CAF50;
}

.logo-img {
    width: 100%;
    height: 100%;
    object-fit: cover;
}

.sidebar-nav {
  padding: 20px 0;
  display: flex;
  flex-direction: column;
  gap: 5px;
}

.nav-item {
  display: flex;
  align-items: center;
  padding: 12px 20px;
  color: #666;
  text-decoration: none;
  transition: all 0.2s;
  cursor: pointer;
}

.nav-item:hover, .nav-item.router-link-active {
  background: #f8f9fa;
  color: #4CAF50;
  border-right: 3px solid #4CAF50;
}

.icon {
  width: 20px;
  height: 20px;
  margin-right: 15px;
}

.logout {
    margin-top: auto;
    color: #e74c3c;
}
.logout:hover {
    color: #c0392b;
    border-right-color: #e74c3c;
}

/* Submenu Styles */
.nav-group { display: flex; flex-direction: column; }
.submenu { background: #fcfcfc; }
.sub-item {
    display: block;
    padding: 10px 20px 10px 55px;
    color: #888;
    text-decoration: none;
    font-size: 0.9rem;
}
.sub-item:hover, .sub-item.router-link-active {
    color: #4CAF50;
    font-weight: bold;
}
.arrow { margin-left: auto; font-size: 0.7rem; color: #aaa; transform: rotate(-90deg); transition: transform 0.2s;}
.arrow.down { transform: rotate(0deg); }
</style>
