#!/bin/bash
version_major=$(echo $(cat ../version.gradle | grep "major = " | sed -n '/.*=/s///p'))
version_minor=$(echo $(cat ../version.gradle | grep "minor = " | sed -n '/.*=/s///p'))
version_patch=$(echo $(cat ../version.gradle | grep "patch = " | sed -n '/.*=/s///p'))
version_major=${version_major// /}
version_minor=${version_minor// /}
version_patch=${version_patch// /}
cd ..
git tag -a v$version_major.$version_minor.$version_patch -m "current version is $version_major.$version_minor.$version_patch"
