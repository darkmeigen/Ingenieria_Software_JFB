/* src/main/resources/static/js/script.js */

// 1. Botón para agregar más filas de productos
document.getElementById('btn-add').addEventListener('click', () => {
    const container = document.getElementById('productos-container');
    const div = document.createElement('div');
    div.classList.add('producto-row');
    div.innerHTML = `
        <div class="form-group">
            <input type="number" class="prod-id" placeholder="ID" required>
        </div>
        <div class="form-group">
            <input type="number" class="prod-cant" placeholder="Cant." required>
        </div>
    `;
    container.appendChild(div);
});

// 2. Manejar el envío del formulario
document.getElementById('facturaForm').addEventListener('submit', async (e) => {
    e.preventDefault(); // Evita que la página se recargue sola

    // Recolectar datos del cliente
    const clienteId = document.getElementById('clienteId').value;

    // Recolectar lista de productos
    const productos = [];
    const filas = document.querySelectorAll('.producto-row');
    
    filas.forEach(fila => {
        const id = fila.querySelector('.prod-id').value;
        const cant = fila.querySelector('.prod-cant').value;
        if(id && cant) {
            productos.push({
                productoId: parseInt(id),
                cantidad: parseInt(cant)
            });
        }
    });

    // Construir el JSON que pide tu Backend
    const facturaJSON = {
        clienteId: parseInt(clienteId),
        productos: productos
    };

    try {
        // Enviar al Backend (POST)
        const response = await fetch('/api/facturas', {
            method: 'POST',
            headers: {
                'Content-Type': 'application/json'
            },
            body: JSON.stringify(facturaJSON)
        });

        if (!response.ok) {
            throw new Error('Error al crear factura (Revise stock o IDs)');
        }

        const data = await response.json();

        // Mostrar resultado
        mostrarResultado(data);

    } catch (error) {
        alert("❌ Error: " + error.message);
        console.error(error);
    }
});

function mostrarResultado(data) {
    document.getElementById('facturaForm').classList.add('hidden'); // Ocultar form
    document.getElementById('resultado').classList.remove('hidden'); // Mostrar ticket
    
    document.getElementById('res-id').innerText = data.idFactura;
    document.getElementById('res-cliente').innerText = data.cliente.nombre;
    // Formatear dinero
    document.getElementById('res-total').innerText = '$' + data.total.toFixed(2);
}