package PluginCore;


import PluginTest.liu.AutoSelect;
import org.apache.ibatis.executor.Executor;
import org.apache.ibatis.mapping.BoundSql;
import org.apache.ibatis.mapping.MappedStatement;
import org.apache.ibatis.mapping.ParameterMapping;
import org.apache.ibatis.mapping.SqlSource;
import org.apache.ibatis.plugin.*;
import org.apache.ibatis.session.ResultHandler;
import org.apache.ibatis.session.RowBounds;

import java.util.Iterator;
import java.util.List;
import java.util.Properties;

/**
 * Created by liuguoyun on 2018/7/14.
 */

@Intercepts({@Signature(
        type = Executor.class,
        method = "query",
        args = {MappedStatement.class, Object.class, RowBounds.class, ResultHandler.class}
)})
public class HelloInterceptor implements Interceptor {


    static int MAPPED_STATEMENT_INDEX = 0;
    static int PARAMETER_INDEX = 1;
    static int ROWBOUNDS_INDEX = 2;
    static int RESULT_HANDLER_INDEX = 3;

    public String getPkgName() {
        return PkgName;
    }

    public void setPkgName(String pkgName) {
        PkgName = pkgName;
    }

    private String PkgName;

    @Override
    public Object intercept(Invocation invocation) throws Throwable {
        System.out.println("This is pluginTest......Very funny, Uh");

        final Executor executor = (Executor)invocation.getTarget();
        Object[] queryArgs = invocation.getArgs();
        final MappedStatement ms = (MappedStatement)queryArgs[MAPPED_STATEMENT_INDEX];

        final Object parameter = queryArgs[PARAMETER_INDEX];


        String sqlRec = ms.getBoundSql(parameter).getSql();

        if(!sqlRec.contains(",")){
            return invocation.proceed();
        }

        String[] VOS = sqlRec.split(",");
        if(!VOS[0].equals(AutoSelect.AUTOSELECTED)){
            return invocation.proceed();
        }

        String[] pkgs = PkgName.split(",");

        GenerateLiuSQL.TabInit(pkgs,ms.getConfiguration().getEnvironment().getDataSource().getConnection());

        Class clz = HelloInterceptor.class.getClassLoader().loadClass(VOS[1]);

        String sql = GenerateLiuSQL.GernerateSelect(clz.newInstance());

        System.out.println("SQL:"+sql);

        final BoundSql boundSql = ms.getBoundSql(parameter);
        queryArgs[MAPPED_STATEMENT_INDEX] = this.copyFromNewSql(ms, boundSql, sql, null, null);

        return (List)invocation.proceed();
    }


    private MappedStatement copyFromNewSql(MappedStatement ms, BoundSql boundSql, String sql, List<ParameterMapping> parameterMappings, Object parameter) {
        BoundSql newBoundSql = this.copyFromBoundSql(ms, boundSql, sql, parameterMappings, parameter);
        return this.copyFromMappedStatement(ms, new OffsetLimitInterceptor.BoundSqlSqlSource(newBoundSql));
    }

    private BoundSql copyFromBoundSql(MappedStatement ms, BoundSql boundSql, String sql, List<ParameterMapping> parameterMappings, Object parameter) {
        BoundSql newBoundSql = new BoundSql(ms.getConfiguration(), sql, parameterMappings, parameter);
        Iterator i$ = boundSql.getParameterMappings().iterator();

        while(i$.hasNext()) {
            ParameterMapping mapping = (ParameterMapping)i$.next();
            String prop = mapping.getProperty();
            if(boundSql.hasAdditionalParameter(prop)) {
                newBoundSql.setAdditionalParameter(prop, boundSql.getAdditionalParameter(prop));
            }
        }

        return newBoundSql;
    }

    private MappedStatement copyFromMappedStatement(MappedStatement ms, SqlSource newSqlSource) {
        MappedStatement.Builder builder = new MappedStatement.Builder(ms.getConfiguration(), ms.getId(), newSqlSource, ms.getSqlCommandType());
        builder.resource(ms.getResource());
        builder.fetchSize(ms.getFetchSize());
        builder.statementType(ms.getStatementType());
        builder.keyGenerator(ms.getKeyGenerator());
        if(ms.getKeyProperties() != null && ms.getKeyProperties().length != 0) {
            StringBuffer keyProperties = new StringBuffer();
            String[] arr$ = ms.getKeyProperties();
            int len$ = arr$.length;

            for(int i$ = 0; i$ < len$; ++i$) {
                String keyProperty = arr$[i$];
                keyProperties.append(keyProperty).append(",");
            }

            keyProperties.delete(keyProperties.length() - 1, keyProperties.length());
            builder.keyProperty(keyProperties.toString());
        }

        builder.timeout(ms.getTimeout());
        builder.parameterMap(ms.getParameterMap());
        builder.resultMaps(ms.getResultMaps());
        builder.resultSetType(ms.getResultSetType());
        builder.cache(ms.getCache());
        builder.flushCacheRequired(ms.isFlushCacheRequired());
        builder.useCache(ms.isUseCache());
        return builder.build();
    }



    @Override
    public Object plugin(Object target) {
        return Plugin.wrap(target, this);
    }

    @Override
    public void setProperties(Properties properties) {
        PropertiesHelper propertiesHelper = new PropertiesHelper(properties);
        String PkgName = propertiesHelper.getRequiredString("PkgName");
        this.setPkgName(PkgName);
    }
}
