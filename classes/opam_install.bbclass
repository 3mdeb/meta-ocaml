inherit opam_base

# configuration and compilation are handled by OPAM during install below,
# license file is also contained in sources obtained by OPAM
do_configure[noexec] = "1"
do_compile[noexec] = "1"
do_populate_lic[noexec] = "1"

bindir = "${OPAM_ROOT}/default/bin"
libdir = "${OPAM_ROOT}/default/lib"

opam_install_do_install () {
    if [ "${OPAM_VERBOSE}" = "1" ]; then
        export OPAMKEEPLOGS="1"
        export OPAMVERBOSE="5"
        export OPAMKEEPBUILDDIR="1"
        export OPAMDEBUG="127"
    fi

    if [ -z "${OPAM_PKGNAME_OVERRIDE}" ]; then
        if [ `expr substr ${PN} 1 5` = "opam-" ]; then
            OPAM_PKGNAME=`expr substr ${PN} 6 99`
        else
            OPAM_PKGNAME=${PN}
        fi
    else
        OPAM_PKGNAME=${OPAM_PKGNAME_OVERRIDE}
    fi

    if [ "${PV}" != "1.0" ]; then
        OPAM_PKGVER=".${PV}"
    else
        OPAM_PKGVER=""
    fi

    echo "installing opam package ${OPAM_PKGNAME}${OPAM_PKGVER}..."

    export prefix=""
    export OPAMROOT="${OPAM_ROOT}"
    eval $(opam env)
    opam install ${OPAM_PKGNAME}${OPAM_PKGVER} --yes
}


EXPORT_FUNCTIONS do_install
