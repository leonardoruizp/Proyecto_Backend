package mi.consultorio.consultoriodental;


import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RestController;

@CrossOrigin
@RestController
public class App {

    @Autowired
    ICita iCita;

    // Obtener todas las citas (GET)
    @RequestMapping(value = "/citas", method = RequestMethod.GET)
    public Iterable<Cita> obtenerCitas() {
        return iCita.findAll();
    }

    // Crear nueva cita (POST)
    @RequestMapping(value = "/citas", method = RequestMethod.POST)
    public void crearCita(@RequestBody Cita cita) {
        cita.setEstado("pendiente");
        iCita.save(cita);
    }

    // Editar cita (PUT)
    @RequestMapping(value = "/citas/{id}", method = RequestMethod.PUT)
    public void actualizarCita(@PathVariable Integer id, @RequestBody Cita cita) {
        cita.setId(id);
        iCita.save(cita);
    }

    // Eliminar cita (DELETE)
    @RequestMapping(value = "/citas/{id}", method = RequestMethod.DELETE)
    public void eliminarCita(@PathVariable Integer id) {
        iCita.deleteById(id);
    }
}

