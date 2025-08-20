package com.JYA_proyecto.JYA_proyecto.service;

import com.JYA_proyecto.JYA_proyecto.dao.SuscripcionDao;
import com.JYA_proyecto.JYA_proyecto.model.Suscripcion;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
public class SuscripcionServiceImpl implements SuscripcionService {

    @Autowired
    private SuscripcionDao suscripcionDao;

    @Override
    @Transactional(readOnly = true)
    public List<Suscripcion> obtenerTodas() {
        return suscripcionDao.findAll();
    }

    @Override
    @Transactional
    public boolean suscribir(String email) {
        try {
            if (email == null || email.trim().isEmpty()) {
                return false;
            }

            // Normalizar email
            email = email.trim().toLowerCase();

            // Verificar si ya existe
            if (existeSuscripcion(email)) {
                return false;
            }

            // Crear nueva suscripción
            Suscripcion suscripcion = new Suscripcion(email);
            suscripcionDao.save(suscripcion);
            return true;

        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
    }

    @Override
    @Transactional(readOnly = true)
    public boolean existeSuscripcion(String email) {
        if (email == null || email.trim().isEmpty()) {
            return false;
        }
        return suscripcionDao.existsByEmail(email.trim().toLowerCase());
    }

    @Override
    @Transactional
    public void eliminarSuscripcion(String email) {
        if (email != null && !email.trim().isEmpty()) {
            suscripcionDao.deleteByEmail(email.trim().toLowerCase());
        }
    }

    @Override
    @Transactional(readOnly = true)
    public long contarSuscripciones() {
        return suscripcionDao.count();
    }
}