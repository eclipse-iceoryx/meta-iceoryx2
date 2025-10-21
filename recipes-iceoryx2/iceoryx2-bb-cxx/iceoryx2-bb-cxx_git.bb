# Copyright (c) 2025 Contributors to the Eclipse Foundation
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

SUMMARY = "iceoryx2 BB C++ Constructs"
DESCRIPTION = "This package contains the iceoryx2 BB C++ Constructs"
HOMEPAGE = "https://iceoryx.io"
BUGTRACKER = "https://github.com/eclipse-iceoryx/iceoryx2/issues"
LICENSE = "Apache-2.0 | MIT"
LIC_FILES_CHKSUM = "file://LICENSE-APACHE;md5=22a53954e4e0ec258dfce4391e905dac \
                    file://LICENSE-MIT;md5=b377b220f43d747efdec40d69fcaa69d"

inherit cmake ptest

DEPENDS = " \
  iceoryx2-cmake-modules \
  googletest \
  "

SRC_URI = "git://github.com/eclipse-iceoryx/iceoryx2.git;protocol=https;branch=main \
          file://run-ptest"
SRCREV = "bf41f8bd45bd8f116a663d32fd9f59490808966b"

S = "${WORKDIR}/git/iceoryx2-bb/cxx"

INSANE_SKIP:${PN} += "already-stripped"
FILES_SOLIBSDEV = ""

EXTRA_OECMAKE += "-DCMAKE_INSTALL_PREFIX=${D}/${exec_prefix} -DBUILD_TESTING=ON -DUSE_SYSTEM_GTEST=ON"

PACKAGES =+ "${PN}-tests"
BBCLASSEXTEND = "native nativesdk"

do_install() {
  cmake --install ${S}/../../../build
}

do_install_ptest() {
    install -d ${D}/${PTEST_PATH}/tests
    install -m 0755  ${B}/tests/iceoryx2-bb-containers-tests ${D}/${PTEST_PATH}/tests
    install -m 0755  ${S}/../../../run-ptest ${D}/${PTEST_PATH}/
}
