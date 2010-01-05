/*

Copyright (C) 2006 Thorsten Berger

This program is free software; you can redistribute it and/or modify
it under the terms of the GNU General Public License as published by
the Free Software Foundation; either version 2 of the License, or
(at your option) any later version.

This program is distributed in the hope that it will be useful,
but WITHOUT ANY WARRANTY; without even the implied warranty of
MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
GNU General Public License for more details.

You should have received a copy of the GNU General Public License
along with this program; if not, write to the Free Software
Foundation, Inc., 59 Temple Place, Suite 330, Boston, MA  02111-1307  USA
 */
package de.elateportal.editor.preview;

import java.io.ByteArrayInputStream;
import java.io.InputStream;
import java.io.UnsupportedEncodingException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import de.thorstenberger.taskmodel.CategoryFilter;
import de.thorstenberger.taskmodel.MethodNotSupportedException;
import de.thorstenberger.taskmodel.TaskApiException;
import de.thorstenberger.taskmodel.TaskCategory;
import de.thorstenberger.taskmodel.TaskContants;
import de.thorstenberger.taskmodel.TaskDef;
import de.thorstenberger.taskmodel.TaskFactory;
import de.thorstenberger.taskmodel.TaskFilter;
import de.thorstenberger.taskmodel.TaskFilterException;
import de.thorstenberger.taskmodel.Tasklet;
import de.thorstenberger.taskmodel.TaskletCorrection;
import de.thorstenberger.taskmodel.UserInfo;
import de.thorstenberger.taskmodel.TaskManager.UserAttribute;
import de.thorstenberger.taskmodel.complex.ComplexTaskBuilder;
import de.thorstenberger.taskmodel.complex.ComplexTasklet;
import de.thorstenberger.taskmodel.complex.ComplexTaskletImpl;
import de.thorstenberger.taskmodel.complex.TaskDef_Complex;
import de.thorstenberger.taskmodel.complex.TaskDef_ComplexImpl;
import de.thorstenberger.taskmodel.complex.complextaskdef.ComplexTaskDefDAO;
import de.thorstenberger.taskmodel.complex.complextaskhandling.ComplexTaskHandlingDAO;
import de.thorstenberger.taskmodel.impl.TaskletCorrectionImpl;

/**
 * @author Thorsten Berger
 * @author Steffen Dienst
 * 
 */
public class DummyTaskFactoryImpl implements TaskFactory {

	public static final String DUMMY_TITLE = "Preview Tasklet";

	private final ComplexTaskDefDAO complexTaskDefDAO;
	private final ComplexTaskHandlingDAO complexTaskHandlingDAO;
	private final ComplexTaskBuilder complexTaskBuilder;

	private String taskdefXml;

	/**
 *
 */
	public DummyTaskFactoryImpl(final ComplexTaskDefDAO complexTaskDefDAO, final ComplexTaskHandlingDAO
	    complexTaskHandlingDAO,
	    final ComplexTaskBuilder complexTaskBuilder) {

		this.complexTaskDefDAO = complexTaskDefDAO;
		this.complexTaskHandlingDAO = complexTaskHandlingDAO;
		this.complexTaskBuilder = complexTaskBuilder;

	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see de.thorstenberger.taskmodel.TaskFactory#availableTypes()
	 */
	public List<String> availableTypes() {
		final List<String> availableTypes = new ArrayList<String>();
		availableTypes.add(TaskContants.TYPE_COMPLEX);
		return availableTypes;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see de.thorstenberger.taskmodel.TaskFactory#availableUserAttributes()
	 */
	public List<UserAttribute> availableUserAttributes() {
		return null;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * de.thorstenberger.taskmodel.TaskFactory#createTasklet(java.lang.String,
	 * long)
	 */
	public Tasklet createTasklet(final String userId, final long taskId)
	    throws TaskApiException {

		final ByteArrayInputStream bais = new ByteArrayInputStream(new byte[0]);

		final TaskletCorrection correction = new TaskletCorrectionImpl(null, null, null, null, null, null);

		final ComplexTasklet tasklet =
		    new ComplexTaskletImpl(this, complexTaskBuilder, userId, (TaskDef_Complex) getTaskDef(taskId),
		    Tasklet.Status.INITIALIZED, null/* taskletVO.getFlags() */, correction, complexTaskHandlingDAO, bais, new
		    HashMap<String, String>());

		return tasklet;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see de.thorstenberger.taskmodel.TaskFactory#deleteTaskCategory(long)
	 */
	public void deleteTaskCategory(final long id) throws MethodNotSupportedException {
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see de.thorstenberger.taskmodel.TaskFactory#deleteTaskDef(long)
	 */
	public void deleteTaskDef(final long id) throws MethodNotSupportedException {
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see de.thorstenberger.taskmodel.TaskFactory#getCategories()
	 */
	public List<TaskCategory> getCategories() {
		return null;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * de.thorstenberger.taskmodel.TaskFactory#getCategories(de.thorstenberger
	 * .taskmodel.CategoryFilter)
	 */
	public List<TaskCategory> getCategories(final CategoryFilter categoryFilter) {
		return null;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see de.thorstenberger.taskmodel.TaskFactory#getCategory(long)
	 */
	public TaskCategory getCategory(final long id) {
		return null;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see de.thorstenberger.taskmodel.TaskFactory#getCorrectors()
	 */
	public List<UserInfo> getCorrectors() {
		return null;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see de.thorstenberger.taskmodel.TaskFactory#getTaskDef(long)
	 */
	public TaskDef getTaskDef(final long taskId) {

		final Long deadline = null;

		InputStream is = null;
		try {
			is = new ByteArrayInputStream(this.taskdefXml.getBytes("UTF-8"));
		} catch (UnsupportedEncodingException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		final TaskDef_ComplexImpl cp = new TaskDef_ComplexImpl(taskId, DUMMY_TITLE,
		    null/* t.getShortDescription() */, deadline, false/* t.isStopped() */, null, complexTaskDefDAO,
		    is, false/* t.getComplexTaskDef().isShowCorrectionToStudents() */, true/*
																																								 * t.
																																								 * isVisible
																																								 * (
																																								 * )
																																								 */);

		return cp;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see de.thorstenberger.taskmodel.TaskFactory#getTaskDefs()
	 */
	public List<TaskDef> getTaskDefs() {
		return null;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * de.thorstenberger.taskmodel.TaskFactory#getTaskDefs(de.thorstenberger.taskmodel
	 * .TaskFilter)
	 */
	public List<TaskDef> getTaskDefs(final TaskFilter filter)
	    throws TaskFilterException {
		return null;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see de.thorstenberger.taskmodel.TaskFactory#getTasklet(java.lang.String,
	 * long)
	 */
	public Tasklet getTasklet(final String userId, final long taskId) {
		try {
			return createTasklet(userId, taskId);
		} catch (TaskApiException e) {
			e.printStackTrace();
			return null;
		}
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see de.thorstenberger.taskmodel.TaskFactory#getTasklets(long)
	 */
	public List<Tasklet> getTasklets(final long taskId) {
		return null;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * de.thorstenberger.taskmodel.TaskFactory#getUserIdsOfAvailableTasklets(long)
	 */
	public List<String> getUserIdsOfAvailableTasklets(final long taskId) {
		return null;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * de.thorstenberger.taskmodel.TaskFactory#getUserIdsOfTaskletsAssignedToCorrector
	 * (long, java.lang.String)
	 */
	public List<String> getUserIdsOfTaskletsAssignedToCorrector(final long taskId,
	    final String correctorId) {
		return null;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see de.thorstenberger.taskmodel.TaskFactory#getUserInfo(java.lang.String)
	 */
	public UserInfo getUserInfo(final String login) {
		return null;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see de.thorstenberger.taskmodel.TaskFactory#logPostData(java.lang.String,
	 * de.thorstenberger.taskmodel.Tasklet, java.lang.String)
	 */
	public void logPostData(final String msg, final Tasklet tasklet, final String ip) {
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see de.thorstenberger.taskmodel.TaskFactory#logPostData(java.lang.String,
	 * java.lang.Throwable, de.thorstenberger.taskmodel.Tasklet, java.lang.String)
	 */
	public void logPostData(final String msg, final Throwable throwable, final Tasklet tasklet,
	    final String ip) {
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * de.thorstenberger.taskmodel.TaskFactory#removeTasklet(java.lang.String,
	 * long)
	 */
	public void removeTasklet(final String userId, final long taskId)
	    throws TaskApiException {
	}

	/**
	 * @param taskdefXml
	 *          the sourceFile to set
	 */
	public void setTaskDefXml(final String xml) {
		this.taskdefXml = xml;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * de.thorstenberger.taskmodel.TaskFactory#storeTaskCategory(de.thorstenberger
	 * .taskmodel.TaskCategory)
	 */
	public void storeTaskCategory(final TaskCategory category) {
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * de.thorstenberger.taskmodel.TaskFactory#storeTaskDef(de.thorstenberger.
	 * taskmodel.TaskDef, long)
	 */
	public void storeTaskDef(final TaskDef taskDef, final long taskCategoryId)
	    throws TaskApiException {
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * de.thorstenberger.taskmodel.TaskFactory#storeTasklet(de.thorstenberger.
	 * taskmodel.Tasklet)
	 */
	public void storeTasklet(final Tasklet tasklet) throws TaskApiException {
	}

}
