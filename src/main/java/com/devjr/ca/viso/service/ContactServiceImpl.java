package com.devjr.ca.viso.service;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.stereotype.Service;

import com.devjr.ca.viso.converter.ContactRequestConverter;
import com.devjr.ca.viso.converter.ContactResponseConverter;
import com.devjr.ca.viso.domain.Contact;
import com.devjr.ca.viso.entity.ContactEntity;
import com.devjr.ca.viso.repository.IContactRepo;
import com.devjr.ca.viso.zutils.UtilsLanguage;
import com.devjr.ca.viso.zutils.UtilsLogs;

/**
 * Representa el Servicio respecto a los Medios de Contacto de una Persona
 * Física/Jurídica o Carrera.
 *
 * @author Jacinto R^2
 * @version 1.0
 * @since 18/04/2020
 * @modify 10/05/2020
 */
@Service
public class ContactServiceImpl implements IContactService {

	private static final Logger LOG = LoggerFactory.getLogger(ContactServiceImpl.class);

	@Autowired
	private IContactRepo repo;

	@Autowired
	private ContactRequestConverter converterRequest;

	@Autowired
	private ContactResponseConverter converterResponse;

	/*********** GET ***********/
	@Override
	public List<Contact> findAll() {
		try {
			final List<ContactEntity> listDAO = this.repo.findAll();
			final List<Contact> list = listDAO.stream().map(ce -> this.converterResponse.convert(ce))
					.collect(Collectors.toList());
			if (list.isEmpty()) {
				final String msg = UtilsLogs.info(this.getClass().getSimpleName(), "findAll",
						UtilsLanguage.MSG_INFO_GET_ALL_BBDD);
				ContactServiceImpl.LOG.info(msg);
			}
			return list;
		} catch (final NullPointerException npe) {
			final String msg = UtilsLogs.error(this.getClass().getSimpleName(), "findAll",
					UtilsLanguage.MSG_ERROR_GET_ALL_BBDD);
			ContactServiceImpl.LOG.error(msg);
			// TODO throw exception personalized
			return new ArrayList<>();
		}
	}

	@Override
	public List<Contact> findAllOrderByEmail() {
		final List<ContactEntity> listDAO = this.repo.findAllOrderByEmail();
		final List<Contact> list = listDAO.stream().map(ce -> this.converterResponse.convert(ce))
				.collect(Collectors.toList());
		if (list.isEmpty()) {
			ContactServiceImpl.LOG.info(UtilsLanguage.MSG_ERROR_GET_ALL_BBDD);
		}
		return list;
	}

	@Override
	public List<Contact> searchByPhoneMobile(final String phoneMobile) {
		final List<ContactEntity> listDAO = this.repo.searchPhoneMovil(phoneMobile);
		final List<Contact> list = listDAO.stream().map(ce -> this.converterResponse.convert(ce))
				.collect(Collectors.toList());
		if (list.isEmpty()) {
			ContactServiceImpl.LOG.info(UtilsLanguage.MSG_ERROR_GET_ALL_BBDD);
		}
		return list;
	}

	@Override
	public List<Contact> searchByPhoneHome(final String phoneHome) {
		final List<ContactEntity> listDAO = this.repo.findByPhoneHomeContainingOrderByEmailAsc(phoneHome);
		final List<Contact> list = listDAO.stream().map(ce -> this.converterResponse.convert(ce))
				.collect(Collectors.toList());
		if (list.isEmpty()) {
			ContactServiceImpl.LOG.info(UtilsLanguage.MSG_ERROR_GET_ALL_BBDD);
		}
		return list;
	}

	@Override
	public Contact findById(final Integer id) {
		final Optional<ContactEntity> opt = this.repo.findById(id);
		if (opt.isPresent()) {
			return this.converterResponse.convert(opt.get());
		}
		ContactServiceImpl.LOG.info(UtilsLanguage.MSG_ERROR_GET_ONE_BBDD);
		return null;
	}

	/*********** POST - PUT - PATCH ***********/
	@Override
	public Contact save(final Contact value) {
		final ContactEntity entity = this.converterRequest.convert(value);
		final Contact res = this.converterResponse.convert(this.repo.save(entity));
		ContactServiceImpl.LOG.info(UtilsLanguage.MSG_OK_ADD_UPDATE_BBDD);
		return res;
	}

	/*********** DELETE ***********/
	@Override
	public void deleteById(final Integer id) {
		try {
			this.repo.deleteById(id);
			ContactServiceImpl.LOG.info(UtilsLanguage.MSG_OK_DELETE_BBDD);
		} catch (final EmptyResultDataAccessException e) {
			ContactServiceImpl.LOG.info(UtilsLanguage.MSG_ERROR_DELETE_BBDD);
		}
	}

}
