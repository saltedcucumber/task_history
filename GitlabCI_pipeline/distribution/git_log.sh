#!/bin/bash
git log --since=2.weeks --pretty=format:"%h - %an, %ar : %s"|grep "Merge" >> releasenotes_debug.txt
git log --since=2.weeks --pretty=format:"%h - %an, %ar : %s"|grep "Merge" >> releasenotes_release.txt