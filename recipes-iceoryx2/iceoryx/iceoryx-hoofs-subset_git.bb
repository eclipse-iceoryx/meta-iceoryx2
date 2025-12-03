SUMMARY = "iceoryx classic C++ base library"
DESCRIPTION = "This package contains the iceoryx classic C++ base library subset, intended to be used with the iceoryx2 C++ bindings."
HOMEPAGE = "https://iceoryx.io"
BUGTRACKER = "https://github.com/eclipse-iceoryx/iceoryx/issues"
LICENSE = "Apache-2.0 | MIT"
LIC_FILES_CHKSUM = "file://LICENSE-APACHE;md5=46d6aa0ba1fa2ed247cd8d42f20b72f4 \
                    file://LICENSE-MIT;md5=b377b220f43d747efdec40d69fcaa69d"

inherit cmake

DEPENDS = "iceoryx-platform-minimal"

SRC_URI = "git://git@github.com/eclipse-iceoryx/iceoryx.git;protocol=https;branch=main"
SRCREV = "ffd361023196d1422f1ff96723d1c83d9d4838ae"

S = "${WORKDIR}/git/iceoryx_hoofs"

EXTRA_OECMAKE = "-DIOX_USE_HOOFS_SUBSET_ONLY=ON"

RDEPENDS:${PN}-dev += "iceoryx-platform-minimal-dev"
RDEPENDS:${PN} += "iceoryx-platform-minimal"
BBCLASSEXTEND = "native nativesdk"
