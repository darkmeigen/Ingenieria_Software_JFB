
<template>
  <div class="home-wrapper">
    <div class="hero-content">
      <h1>Bienvenido al ERP Corporativo</h1>
      <p>Gestión eficiente de facturación electrónica e inventarios.</p>
      
      <div class="stats-grid">
        <div class="stat-card">
          <h3>Inventario</h3>
          <p>Control en tiempo real</p>
        </div>
        <div class="stat-card">
          <h3>Facturación</h3>
          <p>Emisión rápida</p>
        </div>
        <div class="stat-card">
          <h3>Reportes</h3>
          <p>Análisis financiero</p>
        </div>
      </div>

      <router-link to="/login" class="btn btn-primary btn-large">Ingresar al Sistema</router-link>
    </div>
  </div>
</template>

<style scoped>
.home-wrapper {
  text-align: center;
  padding: 50px 20px;
}

h1 { font-size: 2.5rem; color: var(--primary); margin-bottom: 10px; }
p { font-size: 1.2rem; color: #666; margin-bottom: 40px; }

.stats-grid {
  display: flex;
  justify-content: center;
  gap: 20px;
  margin-bottom: 50px;
}

.stat-card {
  background: white;
  padding: 20px;
  border-radius: 8px;
  width: 200px;
  box-shadow: 0 4px 6px rgba(0,0,0,0.05);
}

.stat-card h3 { color: var(--accent); margin: 0; }
.btn-large { font-size: 1.1rem; padding: 15px 40px; }
</style>