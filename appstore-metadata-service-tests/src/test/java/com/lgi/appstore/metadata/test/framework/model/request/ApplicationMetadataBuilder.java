/*
 * If not stated otherwise in this file or this component's LICENSE file the
 * following copyright and licenses apply:
 *
 * Copyright 2022 Liberty Global Technology Services BV
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 * http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package com.lgi.appstore.metadata.test.framework.model.request;

import com.lgi.appstore.metadata.model.Application;
import com.lgi.appstore.metadata.model.ApplicationForUpdate;
import com.lgi.appstore.metadata.model.ApplicationHeaderForUpdate;
import com.lgi.appstore.metadata.model.Category;
import com.lgi.appstore.metadata.model.Dependency;
import com.lgi.appstore.metadata.model.Feature;
import com.lgi.appstore.metadata.model.Hardware;
import com.lgi.appstore.metadata.model.Localization;
import com.lgi.appstore.metadata.model.MaintainerApplicationHeader;
import com.lgi.appstore.metadata.model.Platform;
import com.lgi.appstore.metadata.model.Requirements;
import com.lgi.appstore.metadata.test.framework.model.response.ApplicationDetailsPath;
import com.lgi.appstore.metadata.test.framework.utils.DataUtils;
import org.testcontainers.shaded.org.apache.commons.lang.NotImplementedException;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.function.Function;
import java.util.stream.Collectors;

public class ApplicationMetadataBuilder {
    private String headerId;
    private String headerVersion;
    private String headerName;
    private String headerDescription;
    private String headerType;
    private Integer headerSize;
    private String headerIcon;
    private Boolean headerVisible;
    private String headerOciImageUrl;
    private Category headerCategory;
    private List<Localization> headerLocalizations;
    private Platform platform;
    private List<Dependency> dependencies;
    private List<Feature> features;
    private Hardware hardware;
    private Requirements requirements;

    private ApplicationMetadataBuilder() {
    }

    public static ApplicationMetadataBuilder builder() {
        return new ApplicationMetadataBuilder();
    }

    public ApplicationMetadataBuilder withId(String headerId) {
        this.headerId = headerId;
        return this;
    }

    public ApplicationMetadataBuilder withVersion(String headerVersion) {
        this.headerVersion = headerVersion;
        return this;
    }

    public ApplicationMetadataBuilder withDescription(String headerDescription) {
        this.headerDescription = headerDescription;
        return this;
    }

    public ApplicationMetadataBuilder withType(String headerType) {
        this.headerType = headerType;
        return this;
    }

    public ApplicationMetadataBuilder withSize(Integer headerSize) {
        this.headerSize = headerSize;
        return this;
    }

    public ApplicationMetadataBuilder withName(String headerName) {
        this.headerName = headerName;
        return this;
    }

    public ApplicationMetadataBuilder withIcon(String headerIcon) {
        this.headerIcon = headerIcon;
        return this;
    }

    public ApplicationMetadataBuilder withCategory(Category headerCategory) {
        this.headerCategory = headerCategory;
        return this;
    }

    public ApplicationMetadataBuilder withVisible(Boolean headerVisible) {
        this.headerVisible = headerVisible;
        return this;
    }

    public ApplicationMetadataBuilder withOciImageUrl(String headerOciImageUrl) {
        this.headerOciImageUrl = headerOciImageUrl;
        return this;
    }

    public ApplicationMetadataBuilder withLocalization(String name, String languageCode, String description) {
        if (headerLocalizations == null) {
            headerLocalizations = new ArrayList<>();
        }
        headerLocalizations.add(new Localization().name(name).languageCode(languageCode).description(description));
        return this;
    }

    public ApplicationMetadataBuilder withPlatform(String arch, String os, String variant) {
        this.platform = new Platform().architecture(arch).os(os).variant(variant);
        return this;
    }

    public ApplicationMetadataBuilder withDependency(String id, String version) {
        if (dependencies == null) {
            dependencies = new ArrayList<>();
        }
        dependencies.add(new Dependency().id(id).version(version));
        return this;
    }

    public ApplicationMetadataBuilder withFeature(String name, String version, Boolean required) {
        if (features == null) {
            features = new ArrayList<>();
        }
        features.add(new Feature().name(name).version(version).required(required));
        return this;
    }

    public ApplicationMetadataBuilder withHardware(String cache, String dmpis, String persistent, String ram) {
        this.hardware = new Hardware().cache(cache).dmips(dmpis).persistent(persistent).ram(ram);
        return this;
    }

    private ApplicationMetadataBuilder setFieldValue(String field, Object value) {
        switch (field) {
            case ApplicationDetailsPath.FIELD_VERSION:
                headerVersion = String.valueOf(value);
                break;
            case ApplicationDetailsPath.FIELD_VISIBLE:
                headerVisible = Boolean.valueOf(String.valueOf(value));
                break;
            case ApplicationDetailsPath.FIELD_OCI_IMAGE_URL:
                headerOciImageUrl = String.valueOf(value);
                break;
            case ApplicationDetailsPath.FIELD_NAME:
                headerName = String.valueOf(value);
                break;
            case ApplicationDetailsPath.FIELD_DESCRIPTION:
                headerDescription = String.valueOf(value);
                break;
            case ApplicationDetailsPath.FIELD_CATEGORY:
                headerCategory = Category.fromValue(String.valueOf(value));
                break;
            case ApplicationDetailsPath.FIELD_TYPE:
                headerType = String.valueOf(value);
                break;
            case ApplicationDetailsPath.FIELD_SIZE:
                headerSize = (Integer) value;
                break;
            case ApplicationDetailsPath.FIELD_ICON:
                headerIcon = String.valueOf(value);
                break;
            default:
                throw new NotImplementedException(String.format("Not yet implemented for field %s", field));
        }

        return this;
    }

    public ApplicationMetadataBuilder fromDefaults() {
        this.headerName = DataUtils.randomAppName();
        this.headerVersion = DataUtils.randomAppVersion();
        this.headerDescription = DataUtils.randomAppDescription();
        this.headerType = DataUtils.randomAppHeaderType();
        this.headerSize = 10000000;
        this.headerIcon = DataUtils.randomAppHeaderIcon();
        this.headerVisible = Boolean.TRUE;
        this.headerOciImageUrl = DataUtils.randomOciImageUrl();
        this.headerCategory = Category.APPLICATION;
        this.platform = new Platform().architecture(DataUtils.randomPlatformArch()).os(DataUtils.randomPlatformOs());

        return this;
    }

    public ApplicationMetadataBuilder fromExisting(Application existingApplication) {
        MaintainerApplicationHeader existingHeader = existingApplication.getHeader();

        headerId = existingHeader.getId();
        headerVersion = existingHeader.getVersion();
        headerVisible = existingHeader.isVisible();
        headerOciImageUrl = existingHeader.getOciImageUrl();
        headerType = existingHeader.getType();
        headerCategory = existingHeader.getCategory();
        headerName = existingHeader.getName();
        headerIcon = existingHeader.getIcon();
        headerSize = existingHeader.getSize();
        headerDescription = existingHeader.getDescription();
        headerLocalizations = copyCollection(existingHeader.getLocalization(), this::newLocalization).orElse(null);
        requirements = newRequirements(existingApplication.getRequirements());

        return this;
    }

    public ApplicationMetadataBuilder with(String field, Object value) {
        return setFieldValue(field, value);
    }

    public Application forCreate() {
        final MaintainerApplicationHeader appHeader = new MaintainerApplicationHeader()
                .id(headerId)
                .version(headerVersion)
                .category(headerCategory)
                .name(headerName)
                .description(headerDescription)
                .type(headerType)
                .size(headerSize)
                .icon(headerIcon)
                .visible(headerVisible)
                .ociImageUrl(headerOciImageUrl)
                .localization(headerLocalizations);

        return new Application()
                .header(appHeader)
                .requirements(Optional.ofNullable(requirements).orElse(assembleRequirements()));
    }

    public ApplicationForUpdate forUpdate() {
        ApplicationHeaderForUpdate appHeader = new ApplicationHeaderForUpdate()
                .category(headerCategory)
                .name(headerName)
                .description(headerDescription)
                .type(headerType)
                .size(headerSize)
                .icon(headerIcon)
                .visible(headerVisible)
                .ociImageUrl(headerOciImageUrl)
                .localization(headerLocalizations);

        return new ApplicationForUpdate()
                .header(appHeader)
                .requirements(Optional.ofNullable(requirements).orElse(assembleRequirements()));
    }

    private Requirements assembleRequirements() {
        Requirements requirements = new Requirements();
        Optional.ofNullable(platform).ifPresent(requirements::platform);
        Optional.ofNullable(dependencies).ifPresent(requirements::dependencies);
        Optional.ofNullable(features).ifPresent(requirements::features);
        Optional.ofNullable(hardware).ifPresent(requirements::setHardware);
        return requirements;
    }

    private Requirements newRequirements(Requirements requirements) {
        return new Requirements()
                .features(copyCollection(requirements.getFeatures(), this::newFeature).orElse(null))
                .dependencies(copyCollection(requirements.getDependencies(), this::newDependency).orElse(null))
                .hardware(Optional.ofNullable(requirements.getHardware()).map(this::newHardware).orElse(null))
                .platform(Optional.ofNullable(requirements.getPlatform()).map(this::newPlatform).orElse(null));
    }

    private Localization newLocalization(Localization localization) {
        return new Localization()
                .name(localization.getName())
                .languageCode(localization.getLanguageCode())
                .description(localization.getDescription());
    }

    private <T> Optional<List<T>> copyCollection(List<T> existingCollection, Function<T, T> factoryMethod) {
        return Optional.ofNullable(existingCollection).map(item -> item.stream().map(factoryMethod)
                .collect(Collectors.toList()));
    }

    private Hardware newHardware(Hardware hardware) {
        return new Hardware()
                .cache(hardware.getCache())
                .dmips(hardware.getDmips())
                .persistent(hardware.getPersistent())
                .ram(hardware.getRam());
    }

    private Dependency newDependency(Dependency dependency) {
        return new Dependency()
                .id(dependency.getId())
                .version(dependency.getVersion());
    }

    private Platform newPlatform(Platform platform) {
        return new Platform()
                .os(platform.getOs())
                .variant(platform.getVariant())
                .architecture(platform.getArchitecture());
    }

    private Feature newFeature(Feature feature) {
        return new Feature()
                .name(feature.getName())
                .version(feature.getVersion())
                .required(feature.isRequired());
    }
}