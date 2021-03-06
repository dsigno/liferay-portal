/**
 * Copyright (c) 2000-2013 Liferay, Inc. All rights reserved.
 *
 * This library is free software; you can redistribute it and/or modify it under
 * the terms of the GNU Lesser General Public License as published by the Free
 * Software Foundation; either version 2.1 of the License, or (at your option)
 * any later version.
 *
 * This library is distributed in the hope that it will be useful, but WITHOUT
 * ANY WARRANTY; without even the implied warranty of MERCHANTABILITY or FITNESS
 * FOR A PARTICULAR PURPOSE. See the GNU Lesser General Public License for more
 * details.
 */

package com.liferay.portlet.bookmarks.action;

import com.liferay.portal.kernel.portlet.DefaultConfigurationAction;
import com.liferay.portal.kernel.servlet.SessionErrors;
import com.liferay.portal.kernel.util.GetterUtil;
import com.liferay.portal.kernel.util.LocaleUtil;
import com.liferay.portal.util.PropsValues;
import com.liferay.portlet.bookmarks.model.BookmarksFolderConstants;
import com.liferay.portlet.bookmarks.service.BookmarksFolderLocalServiceUtil;
import com.liferay.portlet.bookmarks.util.BookmarksUtil;
import com.liferay.portlet.documentlibrary.NoSuchFolderException;
import com.liferay.util.ContentUtil;

import javax.portlet.ActionRequest;
import javax.portlet.ActionResponse;
import javax.portlet.PortletConfig;
import javax.portlet.PortletPreferences;
import javax.portlet.PortletRequest;

/**
 * @author Sergio González
 */
public class ConfigurationActionImpl extends DefaultConfigurationAction {

	@Override
	public void postProcessPortletPreferences(
			long companyId, PortletRequest portletRequest,
			PortletPreferences portletPreferences)
		throws Exception {

		removeDefaultValue(
			portletRequest, portletPreferences, "emailFromAddress",
			BookmarksUtil.getEmailFromAddress(portletPreferences, companyId));
		removeDefaultValue(
			portletRequest, portletPreferences, "emailFromName",
			BookmarksUtil.getEmailFromName(portletPreferences, companyId));

		String languageId = LocaleUtil.toLanguageId(
			LocaleUtil.getSiteDefault());

		removeDefaultValue(
			portletRequest, portletPreferences,
			"emailEntryAddedBody_" + languageId,
			ContentUtil.get(PropsValues.BOOKMARKS_EMAIL_ENTRY_ADDED_BODY));
		removeDefaultValue(
			portletRequest, portletPreferences,
			"emailEntryAddedSubject_" + languageId,
			ContentUtil.get(PropsValues.BOOKMARKS_EMAIL_ENTRY_ADDED_SUBJECT));
		removeDefaultValue(
			portletRequest, portletPreferences,
			"emailEntryUpdatedBody_" + languageId,
			ContentUtil.get(PropsValues.BOOKMARKS_EMAIL_ENTRY_UPDATED_BODY));
		removeDefaultValue(
			portletRequest, portletPreferences,
			"emailEntryUpdatedSubject_" + languageId,
			ContentUtil.get(PropsValues.BOOKMARKS_EMAIL_ENTRY_UPDATED_SUBJECT));
	}

	@Override
	public void processAction(
			PortletConfig portletConfig, ActionRequest actionRequest,
			ActionResponse actionResponse)
		throws Exception {

		validateRootFolder(actionRequest);

		super.processAction(portletConfig, actionRequest, actionResponse);
	}

	protected void validateRootFolder(ActionRequest actionRequest)
		throws Exception {

		long rootFolderId = GetterUtil.getLong(
			getParameter(actionRequest, "rootFolderId"));

		if (rootFolderId != BookmarksFolderConstants.DEFAULT_PARENT_FOLDER_ID) {
			try {
				BookmarksFolderLocalServiceUtil.getFolder(rootFolderId);
			}
			catch (NoSuchFolderException nsfe) {
				SessionErrors.add(actionRequest, "rootFolderIdInvalid");
			}
		}
	}

}