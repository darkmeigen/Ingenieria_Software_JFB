package com.facturacion.service;

/*
import com.facturacion.model.Producto;
import com.facturacion.repository.ProductoRepository;
import org.apache.poi.ss.usermodel.*;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

@Service
public class ExcelService {

    @Autowired
    private ProductoRepository productoRepository;

    public void save(MultipartFile file) {
        try {
            List<Producto> productos = excelToProductos(file.getInputStream());
            productoRepository.saveAll(productos);
        } catch (IOException e) {
            throw new RuntimeException("fail to store excel data: " + e.getMessage());
        }
    }

    public List<Producto> excelToProductos(java.io.InputStream is) {
        try {
            Workbook workbook = new XSSFWorkbook(is);
            Sheet sheet = workbook.getSheetAt(0);
            Iterator<Row> rows = sheet.iterator();

            List<Producto> productos = new ArrayList<>();

            int rowNumber = 0;
            while (rows.hasNext()) {
                Row currentRow = rows.next();

                // skip header
                if (rowNumber == 0) {
                    rowNumber++;
                    continue;
                }

                Iterator<Cell> cellsInRow = currentRow.iterator();
                Producto producto = new Producto();

                int cellIdx = 0;
                while (cellsInRow.hasNext()) {
                    Cell currentCell = cellsInRow.next();

                    switch (cellIdx) {
                        case 0:
                            producto.setNombre(currentCell.getStringCellValue());
                            break;
                        case 1:
                            producto.setDescripcion(currentCell.getStringCellValue());
                            break;
                        case 2:
                            producto.setPrecio(currentCell.getNumericCellValue());
                            break;
                        case 3:
                            producto.setStock((int) currentCell.getNumericCellValue());
                            break;
                        default:
                            break;
                    }
                    cellIdx++;
                }

                productos.add(producto);
            }
            workbook.close();
            return productos;
        } catch (IOException e) {
            throw new RuntimeException("fail to parse Excel file: " + e.getMessage());
        }
    }

    public ByteArrayOutputStream generateTemplate() throws IOException {
        Workbook workbook = new XSSFWorkbook();
        Sheet sheet = workbook.createSheet("Productos");

        Row header = sheet.createRow(0);
        header.createCell(0).setCellValue("Nombre");
        header.createCell(1).setCellValue("Descripcion");
        header.createCell(2).setCellValue("Precio");
        header.createCell(3).setCellValue("Stock");

        ByteArrayOutputStream out = new ByteArrayOutputStream();
        workbook.write(out);
        workbook.close();
        return out;
    }
}
*/
public class ExcelService {
    // DUMMY CLASS to allow compilation without POI dependencies
}
