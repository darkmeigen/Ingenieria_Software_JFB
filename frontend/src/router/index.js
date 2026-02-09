import { createRouter, createWebHistory } from 'vue-router'
import LoginView from '../views/LoginView.vue'
import VentasDashboard from '../views/VentasDashboard.vue'
import EmitirFacturaView from '../views/EmitirFacturaView.vue'
import ProductsView from '../views/ProductsView.vue'
import ClientsView from '../views/ClientsView.vue'
import MainLayout from '../components/MainLayout.vue'

const router = createRouter({
  history: createWebHistory(import.meta.env.BASE_URL),
  routes: [
    {
      path: '/',
      component: MainLayout,
      children: [
        { path: '', redirect: '/facturar' },
        { path: 'facturar', component: VentasDashboard },
        { path: 'facturar/nueva', component: EmitirFacturaView },
        { path: 'facturar/historico', component: () => import('../views/HistoricoVentasView.vue') },

        // Products Module
        { path: 'productos', component: () => import('../views/ProductosDashboard.vue') },
        { path: 'productos/inventario', component: ProductsView },

        { path: 'clientes', component: ClientsView }
      ]
    },
    { path: '/login', name: 'login', component: LoginView }
  ]
})

router.beforeEach((to, from, next) => {
  const publicPages = ['/login'];
  const authRequired = !publicPages.includes(to.path);
  const loggedIn = localStorage.getItem('user');

  if (authRequired && !loggedIn) {
    next('/login');
  } else {
    next();
  }
});

export default router