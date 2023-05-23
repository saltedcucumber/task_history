#!/bin/bash
current_version=$(echo $(cat ../version.gradle | grep "minor = " | sed -n '/.*=/s///p'))
future_version=$(echo `expr$(cat ../version.gradle | grep "minor = " | sed -n '/.*=/s///p') + $(echo 1)`)
current_version=${current_version// /}
future_version=${future_version// /}
sed -i "s/minor = $current_version/minor = $future_version/g" ../version.gradle 
current_version=$(echo $(cat ../version.gradle | grep "patch = " | sed -n '/.*=/s///p'))
current_version=${current_version// /}
sed -i "s/patch = $current_version/patch = 0/g" ../version.gradle 