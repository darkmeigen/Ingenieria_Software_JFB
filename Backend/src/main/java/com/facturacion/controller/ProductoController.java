package com.facturacion.controller;

import com.facturacion.model.Producto;
import com.facturacion.service.ProductoService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/productos")
public class ProductoController {

    @Autowired
    private ProductoService productoService;

    @GetMapping
    public List<Producto> listar() {
        return productoService.listarProductos();
    }

    @PostMapping
    public Producto guardar(@RequestBody Producto producto) {
        return productoService.guardarProducto(producto);
    }

    @GetMapping("/{id}")
    public Producto obtener(@PathVariable String id) {
        return productoService.obtenerProducto(id);
    }

    @GetMapping("/buscar")
    public List<Producto> buscar(@RequestParam String query) {
        return productoService.buscarProductos(query);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> eliminar(@PathVariable String id) {
        productoService.eliminarProducto(id);
        return ResponseEntity.noContent().build();
    }

    /*
     * @Autowired
     * private com.facturacion.service.ExcelService excelService;
     * 
     * @PostMapping(value = "/batch", consumes = { "multipart/form-data" })
     * public ResponseEntity<?> uploadFile(@RequestParam("file")
     * org.springframework.web.multipart.MultipartFile file) {
     * if (file.isEmpty()) {
     * return ResponseEntity.badRequest().body("Please select a file to upload");
     * }
     * try {
     * excelService.save(file);
     * return ResponseEntity.ok("Uploaded the file successfully: " +
     * file.getOriginalFilename());
     * } catch (Exception e) {
     * return
     * ResponseEntity.status(org.springframework.http.HttpStatus.EXPECTATION_FAILED)
     * .body("Could not upload the file: " + file.getOriginalFilename() + "!");
     * }
     * }
     * 
     * @GetMapping("/template")
     * public ResponseEntity<byte[]> downloadTemplate() throws java.io.IOException {
     * java.io.ByteArrayOutputStream stream = excelService.generateTemplate();
     * org.springframework.http.HttpHeaders header = new
     * org.springframework.http.HttpHeaders();
     * header.setContentType(new org.springframework.http.MediaType("application",
     * "force-download"));
     * header.set(org.springframework.http.HttpHeaders.CONTENT_DISPOSITION,
     * "attachment; filename=productos_template.xlsx");
     * return new ResponseEntity<>(stream.toByteArray(), header,
     * org.springframework.http.HttpStatus.CREATED);
     * }
     */
}
