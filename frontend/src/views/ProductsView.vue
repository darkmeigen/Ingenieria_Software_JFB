<template>
  <div class="products-container">
    <div class="header">
      <div style="display:flex; align-items:center; gap:10px;">
        <router-link to="/productos" class="btn">← Volver</router-link>
        <h1>Gestión de Productos</h1>
      </div>
      <div class="action-buttons">
          <button @click="showModal = true" class="btn btn-primary">+ Nuevo Producto</button>
      </div>
    </div>

    <!-- ... table container ... -->
    <div class="table-container card">
      <table>
        <thead>
          <tr>
            <th>Nombre</th>
            <th>Descripción</th>
            <th>Precio</th>
            <th>Stock</th>
            <th>Acciones</th>
          </tr>
        </thead>
        <tbody>
          <tr v-for="prod in productos" :key="prod.id">
            <td>{{ prod.nombre }}</td>
            <td>{{ prod.descripcion }}</td>
            <td>${{ prod.precio }}</td>
            <td>{{ prod.stock }}</td>
            <td>
              <button @click="eliminarProducto(prod.id)" class="btn-danger small">Eliminar</button>
            </td>
          </tr>
        </tbody>
      </table>
    </div>

    <!-- Modal Form -->
    <div v-if="showModal" class="modal-overlay">
      <div class="modal card">
        <h3>Nuevo Producto</h3>
        <form @submit.prevent="guardarProducto">
          <label>Nombre</label>
          <input v-model="form.nombre" required>
          
          <label>Descripción</label>
          <input v-model="form.descripcion">
          
          <div class="row">
            <div class="col">
              <label>Precio</label>
              <input type="number" step="0.01" v-model="form.precio" required>
            </div>
            <div class="col">
              <label>Stock</label>
              <input type="number" v-model="form.stock" required>
            </div>
          </div>

          <div class="actions">
            <button type="button" @click="showModal = false" class="btn">Cancelar</button>
            <button type="submit" class="btn btn-primary">Guardar</button>
          </div>
        </form>
      </div>
    </div>
  </div>
</template>

<script setup>
import { ref, onMounted } from 'vue';

const productos = ref([]);
const showModal = ref(false);
const form = ref({ nombre: '', descripcion: '', precio: 0, stock: 0 });

const fetchProductos = async () => {
  try {
    const res = await fetch('http://localhost:8080/api/productos');
    productos.value = await res.json();
  } catch (e) {
    console.error(e);
  }
};

const guardarProducto = async () => {
  // Validation
  const nameRegex = /^[A-Za-z0-9ñÑáéíóúÁÉÍÓÚ\s]+$/;
  if (!nameRegex.test(form.value.nombre)) {
      alert('El nombre solo puede contener letras, números y espacios.');
      return;
  }

  try {
    const res = await fetch('http://localhost:8080/api/productos', {
      method: 'POST',
      headers: { 'Content-Type': 'application/json' },
      body: JSON.stringify(form.value)
    });
    if (res.ok) {
      showModal.value = false;
      form.value = { nombre: '', descripcion: '', precio: 0, stock: 0 };
      fetchProductos();
    }
  } catch (e) {
    alert('Error al guardar');
  }
};

const eliminating = ref(false);
const eliminarProducto = async (id) => {
  if(!confirm('¿Eliminar?')) return;
  await fetch(`http://localhost:8080/api/productos/${id}`, { method: 'DELETE' });
  fetchProductos();
};

onMounted(fetchProductos);
</script>

<style scoped>
.products-container { padding: 20px; }
.header { display: flex; justify-content: space-between; align-items: center; margin-bottom: 20px; }
.action-buttons { display: flex; gap: 10px; }
table { width: 100%; border-collapse: collapse; }
th, td { padding: 12px; text-align: left; border-bottom: 1px solid #eee; }
.btn-danger { background: #ff4444; color: white; border: none; padding: 5px 10px; cursor: pointer; border-radius: 4px;}
.modal-overlay { position: fixed; top: 0; left: 0; right: 0; bottom: 0; background: rgba(0,0,0,0.5); display: flex; justify-content: center; align-items: center; }
.modal { width: 400px; padding: 20px; background: white; border-radius: 8px; }
.actions { display: flex; justify-content: flex-end; gap: 10px; margin-top: 20px; }

.alert-info { background: #e3f2fd; color: #0d47a1; padding: 10px; border-radius: 4px; margin-bottom: 20px; font-size: 0.9rem; }
</style>
