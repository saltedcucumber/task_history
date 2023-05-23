#!/bin/bash
current_version=$(echo $(cat ../version.gradle | grep "patch = " | sed -n '/.*=/s///p'))
future_version=$(echo `expr$(cat ../version.gradle | grep "patch = " | sed -n '/.*=/s///p') + $(echo 1)`)
current_version=${current_version// /}
future_version=${future_version// /}
sed -i "s/patch = $current_version/patch = $future_version/g" ../version.gradle 
