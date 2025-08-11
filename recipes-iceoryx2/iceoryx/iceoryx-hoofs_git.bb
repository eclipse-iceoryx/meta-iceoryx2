SUMMARY = "iceoryx-hoofs"
LICENSE = "Apache-2.0"
LIC_FILES_CHKSUM = "file://LICENSE;md5=46d6aa0ba1fa2ed247cd8d42f20b72f4"

inherit cmake

DEPENDS = " iceoryx-platform"

SRC_URI = "git://git@github.com/eclipse-iceoryx/iceoryx.git;protocol=ssh;branch=main"
SRCREV = "a22f16a12192a007e0695ba4375fe2f4913d5733"

S = "${WORKDIR}/git/iceoryx_hoofs"

EXTRA_OECMAKE = "-DIOX_USE_HOOFS_SUBSET_ONLY=ON"
