SUMMARY = "iceoryx2 CMake modules"
DESCRIPTION = "This package contains the iceoryx2 CMake modules"
HOMEPAGE = "https://iceoryx.io"
BUGTRACKER = "https://github.com/eclipse-iceoryx/iceoryx2/issues"
LICENSE = "Apache-2.0 | MIT"
LIC_FILES_CHKSUM = "file://LICENSE-APACHE;md5=22a53954e4e0ec258dfce4391e905dac \
                    file://LICENSE-MIT;md5=b377b220f43d747efdec40d69fcaa69d"

inherit cmake

DEPENDS = ""

SRC_URI = "git://github.com/eclipse-iceoryx/iceoryx2.git;protocol=https;branch=main"
SRCREV = "b4042aa2aafe5f0d613a263e7cd4bbf8afb4a34e"

S = "${WORKDIR}/git/iceoryx2-cmake-modules"

EXTRA_OECMAKE += "-DRUST_BUILD_ARTIFACT_PATH=${IOX2_STAGING_DIR}"
EXTRA_OECMAKE += "-DCMAKE_INSTALL_PREFIX=${D}/${exec_prefix}"

BBCLASSEXTEND = "native nativesdk"

do_install() {
  cmake --install ${S}/../../build
}
