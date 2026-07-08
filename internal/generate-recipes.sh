#!/usr/bin/env bash
# Copyright (c) 2026 Contributors to the Eclipse Foundation
#
# See the NOTICE file(s) distributed with this work for additional
# information regarding copyright ownership.
#
# This program and the accompanying materials are made available under the
# terms of the Apache Software License 2.0 which is available at
# https://www.apache.org/licenses/LICENSE-2.0, or the MIT license
# which is available at https://opensource.org/licenses/MIT.
#
# SPDX-License-Identifier: Apache-2.0 OR MIT

set -e

C_OFF='\033[0m'
C_BOLD='\033[1m'
C_RED='\033[1;31m'
C_GREEN='\033[1;32m'
C_YELLOW='\033[1;33m'
C_BLUE='\033[1;34m'

YES=1
SKIP=2

RECIPE_VERSION="git"
HAS_RECIPE_VERSION_SET=false

COMMIT_HASH=""
HAS_COMMIT_HASH_SET=false

RECIPE_PATH_LIST=(
    "recipes-iceoryx2/iceoryx2/iceoryx2"
    "recipes-iceoryx2/iceoryx2/iceoryx2-validation-suite"
    "recipes-iceoryx2/iceoryx2-bb-cxx/iceoryx2-bb-cxx"
    "recipes-iceoryx2/iceoryx2-c/iceoryx2-c"
    "recipes-iceoryx2/iceoryx2-c-examples/iceoryx2-c-examples"
    "recipes-iceoryx2/iceoryx2-cmake-modules/iceoryx2-cmake-modules"
    "recipes-iceoryx2/iceoryx2-cxx/iceoryx2-cxx"
    "recipes-iceoryx2/iceoryx2-cxx-examples/iceoryx2-cxx-examples"
)

while (( "$#" )); do
    case "$1" in
        --recipe-version)
            RECIPE_VERSION=$2
            HAS_RECIPE_VERSION_SET=true
            shift 2
            ;;
        --commit-hash)
            COMMIT_HASH=$2
            HAS_COMMIT_HASH_SET=true
            shift 2
            ;;
        "help")
            echo -e "Script to create Yocto recipes for iceoryx2"
            echo -e ""
            echo -e ""
            echo -e "Usage: ${C_GREEN}$(basename $0)${C_OFF} ${C_BLUE}SCRIPT-OPTION${C_OFF}"
            echo -e "Command:"
            echo -e "    help                          Print this help"
            echo -e "Options:"
            echo -e "    "
            echo -e "    --commit-hash <HASH>          The iceoryx2 commit has used for the recipes."
            echo -e "                                  NOTE: Must be the long commit hash with 40 characters."
            echo -e "    --recipe-version <VERSION>    Create or overwrite recipes with the specified <VERSION>"
            echo -e "                                  NOTE: Can be either 'git' (default) or 'x.y.z'"
            echo -e ""
            exit 0
            ;;
        *)
            echo -e "${C_RED}ERROR:${C_OFF} Invalid argument '$1'. Try 'help' for options."
            exit 1
            ;;
    esac
done

print_default_user_exit_hint() {
    echo -e "Canceled script execution!"
}

SELECTION=-1
function show_default_selector() {
    EXIT_HINT=${1:-print_default_user_exit_hint}
    while true; do
        read -p "Yes, Cancel or Skip (Y/C/S) [default=Y]: " yns
        yns=${yns:-Y}
        case $yns in
            [Yy]*)
                SELECTION=${YES}
                break;
                ;;
            [Cc]*)
                $EXIT_HINT
                exit 1
                ;;
            [Ss]*)
                SELECTION=${SKIP}
                break;
                ;;
            *) echo -e "${C_YELLOW}Please use either 'Y', 'C' or 'S'.${C_OFF}";;
        esac
    done
}

if ! [[ ${RECIPE_VERSION} == "git" || ${RECIPE_VERSION} =~ ^[0-9]+\.[0-9]+\.[0-9]+$ ]]; then
    echo -e "${C_RED}ERROR:${C_OFF} Invalid version format for recipe version!"
    echo -e "Expected X.Y.Z (e.g., 1.2.3)! but got '${RECIPE_VERSION}'" >&2
    exit 1
fi

if [[ ${HAS_COMMIT_HASH_SET} == false ]];then
    echo -e "${C_RED}ERROR:${C_OFF} No commit hast set! Please provide the iceoryx2 commit hash with '--commit-hash <HASH>'" >&2
    exit 1
fi
if ! [[ ${COMMIT_HASH} =~ ^[0-9a-fA-F]{40}$ ]]; then
    echo -e "${C_RED}ERROR:${C_OFF} Invalid commit hash!"
    echo -e "Expected full commit hash with 40 characters but got '${COMMIT_HASH}'" >&2
    exit 1
fi

cd $(git rev-parse --show-toplevel)

if [[ ${HAS_RECIPE_VERSION_SET} == false ]];then
    echo -e "${C_BLUE}INFO:${C_OFF} The recipe version is not set! Using ${C_YELLOW}${RECIPE_VERSION}${C_OFF} as recipes version." >&2
fi

echo -e "Shall the ${C_YELLOW}${RECIPE_VERSION}${C_OFF} recipes with iceoryx2 ${C_YELLOW}${COMMIT_HASH}${C_OFF} commit hash be created?"
show_default_selector
if [[ ${SELECTION} == ${YES} ]]; then
    echo -e "Generating"
    for RECIPE in "${RECIPE_PATH_LIST[@]}"; do
        RECIPE_FULL="${RECIPE}_${RECIPE_VERSION}.bb"
        echo -e "  ${RECIPE_FULL}"
        echo 'require ${BPN}.inc' > ${RECIPE_FULL}
        echo '' >> ${RECIPE_FULL}
        echo 'SRCREV = "'${COMMIT_HASH}'"' >> ${RECIPE_FULL}
        echo '' >> ${RECIPE_FULL}
    done
fi

if [[ $RECIPE_VERSION != "git" ]]; then
    echo -e ""
    echo -e "Shall the ${C_YELLOW}ICEORYX2_VERSION${C_OFF} in ${C_YELLOW}conf/layer.conf${C_OFF} set to ${C_YELLOW}${RECIPE_VERSION}${C_OFF}?"
    show_default_selector
    if [[ ${SELECTION} == ${YES} ]]; then
        sed -i "s|^ICEORYX2_VERSION ??= .*|ICEORYX2_VERSION ??= \"${RECIPE_VERSION}\"|" conf/layer.conf
    fi
fi

echo -e ""
echo -e "${C_GREEN}FINISHED${C_OFF}"
