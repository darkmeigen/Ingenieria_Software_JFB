<template>
  <div class="clients-container">
    <div class="header">
      <h1>Gestión de Clientes</h1>
      <button @click="showModal = true" class="btn btn-primary">+ Nuevo Cliente</button>
    </div>

    <div class="table-container card">
      <table>
        <thead>
          <tr>
            <th>Nombre</th>
            <th>ID / RUC</th>
            <th>Email</th>
            <th>Dirección</th>
            <th>Acciones</th>
          </tr>
        </thead>
        <tbody>
          <tr v-for="client in clientes" :key="client.id">
            <td>{{ client.nombre }}</td>
            <td>{{ client.identificacion }}</td>
            <td>{{ client.email }}</td>
            <td>{{ client.direccion }}</td>
            <td>
              <button @click="eliminarCliente(client.id)" class="btn-danger small">Eliminar</button>
            </td>
          </tr>
        </tbody>
      </table>
    </div>

    <!-- Modal Form -->
    <div v-if="showModal" class="modal-overlay">
      <div class="modal card">
        <h3>Nuevo Cliente</h3>
        <form @submit.prevent="guardarCliente">
          <label>Nombre Completo</label>
          <input v-model="form.nombre" required>
          
          <label>Identificación (RUC/Cédula)</label>
          <input v-model="form.identificacion" required>
          
          <label>Email</label>
          <input type="email" v-model="form.email">
          
          <label>Dirección</label>
          <input v-model="form.direccion">

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

const clientes = ref([]);
const showModal = ref(false);
const form = ref({ nombre: '', identificacion: '', email: '', direccion: '' });

const fetchClientes = async () => {
  try {
    const res = await fetch('http://localhost:8080/api/clientes');
    clientes.value = await res.json();
  } catch (e) {
    console.error(e);
  }
};

const guardarCliente = async () => {
  try {
    const res = await fetch('http://localhost:8080/api/clientes', {
      method: 'POST',
      headers: { 'Content-Type': 'application/json' },
      body: JSON.stringify(form.value)
    });
    if (res.ok) {
      showModal.value = false;
      form.value = { nombre: '', identificacion: '', email: '', direccion: '' };
      fetchClientes();
    }
  } catch (e) {
    alert('Error al guardar');
  }
};

const eliminarCliente = async (id) => {
  if(!confirm('¿Eliminar?')) return;
  await fetch(`http://localhost:8080/api/clientes/${id}`, { method: 'DELETE' });
  fetchClientes();
};

onMounted(fetchClientes);
</script>

<style scoped>
.clients-container { padding: 20px; }
.header { display: flex; justify-content: space-between; align-items: center; margin-bottom: 20px; }
table { width: 100%; border-collapse: collapse; }
th, td { padding: 12px; text-align: left; border-bottom: 1px solid #eee; }
.btn-danger { background: #ff4444; color: white; border: none; padding: 5px 10px; cursor: pointer; border-radius: 4px;}
.modal-overlay { position: fixed; top: 0; left: 0; right: 0; bottom: 0; background: rgba(0,0,0,0.5); display: flex; justify-content: center; align-items: center; }
.modal { width: 400px; }
.actions { display: flex; justify-content: flex-end; gap: 10px; margin-top: 20px; }
</style>
