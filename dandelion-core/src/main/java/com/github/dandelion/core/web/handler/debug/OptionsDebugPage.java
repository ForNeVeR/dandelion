/*
 * [The "BSD licence"]
 * Copyright (c) 2013-2014 Dandelion
 * All rights reserved.
 *
 * Redistribution and use in source and binary forms, with or without
 * modification, are permitted provided that the following conditions
 * are met:
 *
 * 1. Redistributions of source code must retain the above copyright
 * notice, this list of conditions and the following disclaimer.
 * 2. Redistributions in binary form must reproduce the above copyright
 * notice, this list of conditions and the following disclaimer in the
 * documentation and/or other materials provided with the distribution.
 * 3. Neither the name of Dandelion nor the names of its contributors
 * may be used to endorse or promote products derived from this software
 * without specific prior written permission.
 *
 * THIS SOFTWARE IS PROVIDED BY THE AUTHOR ``AS IS'' AND ANY EXPRESS OR
 * IMPLIED WARRANTIES, INCLUDING, BUT NOT LIMITED TO, THE IMPLIED WARRANTIES
 * OF MERCHANTABILITY AND FITNESS FOR A PARTICULAR PURPOSE ARE DISCLAIMED.
 * IN NO EVENT SHALL THE AUTHOR BE LIABLE FOR ANY DIRECT, INDIRECT,
 * INCIDENTAL, SPECIAL, EXEMPLARY, OR CONSEQUENTIAL DAMAGES (INCLUDING, BUT
 * NOT LIMITED TO, PROCUREMENT OF SUBSTITUTE GOODS OR SERVICES; LOSS OF USE,
 * DATA, OR PROFITS; OR BUSINESS INTERRUPTION) HOWEVER CAUSED AND ON ANY
 * THEORY OF LIABILITY, WHETHER IN CONTRACT, STRICT LIABILITY, OR TORT
 * (INCLUDING NEGLIGENCE OR OTHERWISE) ARISING IN ANY WAY OUT OF THE USE OF
 * THIS SOFTWARE, EVEN IF ADVISED OF THE POSSIBILITY OF SUCH DAMAGE.
 */
package com.github.dandelion.core.web.handler.debug;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.github.dandelion.core.config.Configuration;
import com.github.dandelion.core.config.DandelionConfig;
import com.github.dandelion.core.utils.ResourceUtils;
import com.github.dandelion.core.web.handler.HandlerContext;

/**
 * <p>
 * Debug page focused on global options.
 * </p>
 * <p>
 * This page displays all options currently enabled in the application.
 * </p>
 * 
 * @author Thibault Duchateau
 * @since 0.11.0
 */
public class OptionsDebugPage extends AbstractDebugPage {

	public static final String PAGE_ID = "options";
	public static final String PAGE_NAME = "Options";
	private static final String PAGE_LOCATION = "META-INF/resources/ddl-debugger/html/core-options.html";

	@Override
	public String getId() {
		return PAGE_ID;
	}

	@Override
	public String getName() {
		return PAGE_NAME;
	}

	@Override
	public String getTemplate(HandlerContext context) throws IOException {
		return ResourceUtils.getContentFromInputStream(Thread.currentThread().getContextClassLoader()
				.getResourceAsStream(PAGE_LOCATION));
	}

	@Override
	public Map<String, String> getCustomParameters(HandlerContext context) {

		Configuration conf = context.getContext().getConfiguration();
		
		Map<String, Object> ctx = new HashMap<String, Object>();

		List<Map<String, Object>> options = new ArrayList<Map<String,Object>>();

		options.add(option("Active profile", conf.getActiveProfile()));

		// Asset-related options
		options.add(option(DandelionConfig.ASSET_MINIFICATION.getName(), conf.isAssetMinificationEnabled()));
		options.add(option(DandelionConfig.ASSET_LOCATIONS_RESOLUTION_STRATEGY.getName(), conf.getAssetLocationsResolutionStrategy()));
		options.add(option(DandelionConfig.ASSET_PROCESSORS.getName(), conf.getAssetProcessors()));
		options.add(option(DandelionConfig.ASSET_JS_EXCLUDES.getName(), conf.getAssetJsExcludes()));
		options.add(option(DandelionConfig.ASSET_CSS_EXCLUDES.getName(), conf.getAssetCssExcludes()));
		options.add(option(DandelionConfig.ASSET_URL_PATTERN.getName(), conf.getAssetUrlPattern()));
		options.add(option(DandelionConfig.ASSET_STORAGE.getName(), conf.getAssetStorage()));

		// Versioning-related options
		options.add(option(DandelionConfig.ASSET_VERSIONING_MODE.getName(), conf.getAssetVersioningMode()));
		options.add(option(DandelionConfig.ASSET_VERSIONING_STRATEGY.getName(), conf.getAssetVersioningStrategy()));
		options.add(option(DandelionConfig.ASSET_FIXED_VERSION_TYPE.getName(), conf.getAssetFixedVersionType()));
		options.add(option(DandelionConfig.ASSET_FIXED_VERSION_VALUE.getName(), conf.getAssetFixedVersionValue()));
		options.add(option(DandelionConfig.ASSET_FIXED_VERSION_DATEPATTERN.getName(), conf.getAssetFixedVersionDatePattern()));
		options.add(option(DandelionConfig.ASSET_FIXED_VERSION_TYPE.getName(), conf.getAssetFixedVersionType()));

		// Caching-related options
		options.add(option(DandelionConfig.CACHE.getName(), conf.isCachingEnabled()));
		options.add(option(DandelionConfig.CACHE_NAME.getName(), conf.getCacheName()));
		options.add(option(DandelionConfig.CACHE_MAX_SIZE.getName(), conf.getCacheMaxSize()));
		options.add(option(DandelionConfig.CACHE_MANAGER_NAME.getName(), conf.getCacheManagerName()));
		options.add(option(DandelionConfig.CACHE_CONFIGURATION_LOCATION.getName(), conf.getCacheConfigurationLocation()));

		// Bundle-related options
		options.add(option(DandelionConfig.BUNDLE_LOCATION.getName(), conf.getBundleLocation()));
		options.add(option(DandelionConfig.BUNDLE_INCLUDES.getName(), conf.getBundleIncludes()));
		options.add(option(DandelionConfig.BUNDLE_EXCLUDES.getName(), conf.getBundleExcludes()));

		// Tooling-related options
		options.add(option(DandelionConfig.TOOL_GZIP.getName(), conf.isToolGzipEnabled()));
		options.add(option(DandelionConfig.TOOL_GZIP_MIME_TYPES.getName(), conf.getToolGzipMimeTypes()));
		options.add(option(DandelionConfig.TOOL_DEBUGGER.getName(), conf.isToolDebuggerEnabled()));
		options.add(option(DandelionConfig.TOOL_BUNDLE_RELOADING.getName(), conf.isToolBundleReloadingEnabled()));
		
		// Monitoring-related options
		options.add(option(DandelionConfig.MONITORING_JMX.getName(), conf.isMonitoringJmxEnabled()));

		// Misc options
		options.add(option(DandelionConfig.ENCODING.getName(), conf.getEncoding()));

		ctx.put("options", options);
		ctx.put("page-header", PAGE_NAME);
		
		Map<String, String> params = new HashMap<String, String>();
		try {
			params.put("%MUSTACHE_CTX%", mapper.writeValueAsString(ctx));
		}
		catch (JsonProcessingException e) {
			e.printStackTrace();
		}
		return params;
	}
	
	private Map<String, Object> option(String name, Object value) {
		Map<String, Object> option = new HashMap<String, Object>();
		option.put("name", name);
		option.put("value", value);
		return option;
	}
}
